package com.services;

import com.model.Configuration;
import com.model.Game;
import com.model.Player;
import com.persistence.IConfigurationRepository;
import com.persistence.IGameRepository;
import com.persistence.IPlayerRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameService {
    private static final Logger logger = LogManager.getLogger(GameService.class);

    private final IGameRepository gameRepo;
    private final IPlayerRepository playerRepo;
    private final IConfigurationRepository configRepo;

    // Game state tracking
    private final Map<Integer, Set<String>> guessedWordsMap = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> attemptsMap = new ConcurrentHashMap<>();

    // Observer pattern for ranking updates
    private final List<GameObserver> observers = new ArrayList<>();

    public GameService(IGameRepository gameRepo, IPlayerRepository playerRepo, IConfigurationRepository configRepo) {
        this.gameRepo = gameRepo;
        this.playerRepo = playerRepo;
        this.configRepo = configRepo;
        logger.info("Game service initialized");
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        List<Game> ranking = gameRepo.getAllGamesForRanking();
        for (GameObserver observer : observers) {
            observer.rankingUpdated(ranking);
        }
    }

    /**
     * Start a new game for a player
     */
    public Game startGame(String playerAlias) throws ServiceException {
        logger.info("Starting game for player: {}", playerAlias);

        Player player = playerRepo.findByAlias(playerAlias);
        if (player == null) {
            logger.error("Player not found: {}", playerAlias);
            throw new ServiceException("Player not found");
        }

        Configuration config = configRepo.getRandomConfiguration();
        if (config == null) {
            logger.error("No configurations available");
            throw new ServiceException("No configurations available");
        }

        Game game = new Game(0, player, config, LocalDateTime.now(), 0, 0);
        gameRepo.save(game);

        // Initialize tracking for this game
        guessedWordsMap.put(game.getId(), new HashSet<>());
        attemptsMap.put(game.getId(), 0);

        logger.info("Game started with ID: {}", game.getId());
        return game;
    }

    /**
     * Process a word guess for a game
     */
    public GuessResult processWordGuess(Integer gameId, String guessedWord) throws ServiceException {
        logger.info("Processing guess for game {}: {}", gameId, guessedWord);

        Game game = gameRepo.findOne(gameId);
        if (game == null) {
            logger.error("Game not found: {}", gameId);
            throw new ServiceException("Game not found");
        }

        // Get game state
        Set<String> guessedWords = guessedWordsMap.get(gameId);
        int attempts = attemptsMap.get(gameId);

        // Check if game is over (4 attempts already made)
        if (attempts >= 4) {
            logger.info("Game {} already ended (max attempts reached)", gameId);
            return new GuessResult(false, 0, "Game already ended", true);
        }

        // Increment attempt counter
        attemptsMap.put(gameId, attempts + 1);
        boolean lastAttempt = (attempts + 1) >= 4;

        // Check if word is valid (only using available letters)
        if (!isValidWord(guessedWord, game.getConfiguration().getLetters())) {
            logger.info("Invalid word: {} - not made of available letters", guessedWord);
            return new GuessResult(false, 0, "Word is not made of available letters", lastAttempt);
        }

        // Check if word was already guessed
        if (guessedWords.contains(guessedWord.toLowerCase())) {
            logger.info("Word already guessed: {}", guessedWord);
            return new GuessResult(false, 0, "Word already guessed", lastAttempt);
        }

        // Check if word is one of the target words
        Configuration config = game.getConfiguration();
        boolean isCorrect = isTargetWord(guessedWord, config);

        if (isCorrect) {
            logger.info("Correct word guessed: {}", guessedWord);

            // Add to guessed words
            guessedWords.add(guessedWord.toLowerCase());
            guessedWordsMap.put(gameId, guessedWords);

            // Update score and correct word count
            int newScore = game.getScore() + 10;  // 10 points per correct word
            int newWordCount = game.getNrOfCorrectWords() + 1;

            game.setScore(newScore);
            game.setNrOfCorrectWords(newWordCount);
            gameRepo.update(game);

            // Check if all words are guessed
            if (guessedWords.size() == 4) {
                logger.info("All words guessed for game {}", gameId);
                return new GuessResult(true, 10, "Correct! All words guessed!", true);
            }

            return new GuessResult(true, 10, "Correct word!", lastAttempt);
        } else {
            // Calculate partial matches for hint
            int matches = findBestPartialMatch(guessedWord, config, guessedWords);
            String message = matches > 0 ?
                    "Wrong word. Hint: " + matches + " letter(s) match with a target word." :
                    "Wrong word. Try again.";

            return new GuessResult(false, 0, message, lastAttempt);
        }
    }

    /**
     * Finish a game and return final results
     */
    public GameResult finishGame(Integer gameId) throws ServiceException {
        logger.info("Finishing game: {}", gameId);

        Game game = gameRepo.findOne(gameId);
        if (game == null) {
            logger.error("Game not found: {}", gameId);
            throw new ServiceException("Game not found");
        }

        // Clean up tracking data
        guessedWordsMap.remove(gameId);
        attemptsMap.remove(gameId);

        // Get all possible words for this configuration
        List<String> allWords = getAllWords(game.getConfiguration());

        // Get current ranking
        List<Game> ranking = gameRepo.getAllGamesForRanking();
        int position = findPositionInRanking(game, ranking);

        // Notify observers about the ranking update
        notifyObservers();

        logger.info("Game {} finished with score: {}, correct words: {}",
                gameId, game.getScore(), game.getNrOfCorrectWords());

        return new GameResult(game, allWords, position, ranking);
    }

    /**
     * Check if a word is valid (made up of available letters)
     */
    private boolean isValidWord(String word, String availableLetters) {
        if (word == null || word.isEmpty()) return false;

        // Convert to lowercase for case-insensitive comparison
        word = word.toLowerCase();
        availableLetters = availableLetters.toLowerCase();

        // Check if each letter in the word is available
        for (char c : word.toCharArray()) {
            if (availableLetters.indexOf(c) == -1) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if a word matches one of the target words
     */
    private boolean isTargetWord(String word, Configuration config) {
        word = word.toLowerCase();
        return word.equals(config.getWord1().toLowerCase()) ||
                word.equals(config.getWord2().toLowerCase()) ||
                word.equals(config.getWord3().toLowerCase()) ||
                word.equals(config.getWord4().toLowerCase());
    }

    /**
     * Find the best partial match between the guessed word and any target word
     */
    private int findBestPartialMatch(String guess, Configuration config, Set<String> alreadyGuessed) {
        int maxMatches = 0;
        guess = guess.toLowerCase();

        // Check against each unguessed word
        if (!alreadyGuessed.contains(config.getWord1().toLowerCase())) {
            maxMatches = Math.max(maxMatches, countMatchingLetters(guess, config.getWord1()));
        }
        if (!alreadyGuessed.contains(config.getWord2().toLowerCase())) {
            maxMatches = Math.max(maxMatches, countMatchingLetters(guess, config.getWord2()));
        }
        if (!alreadyGuessed.contains(config.getWord3().toLowerCase())) {
            maxMatches = Math.max(maxMatches, countMatchingLetters(guess, config.getWord3()));
        }
        if (!alreadyGuessed.contains(config.getWord4().toLowerCase())) {
            maxMatches = Math.max(maxMatches, countMatchingLetters(guess, config.getWord4()));
        }

        return maxMatches;
    }

    /**
     * Count how many letters match in the correct positions
     */
    private int countMatchingLetters(String guess, String target) {
        guess = guess.toLowerCase();
        target = target.toLowerCase();
        int matches = 0;
        int minLength = Math.min(guess.length(), target.length());

        for (int i = 0; i < minLength; i++) {
            if (guess.charAt(i) == target.charAt(i)) {
                matches++;
            }
        }

        return matches;
    }

    private List<String> getAllWords(Configuration config) {
        List<String> words = new ArrayList<>();
        words.add(config.getWord1());
        words.add(config.getWord2());
        words.add(config.getWord3());
        words.add(config.getWord4());
        return words;
    }

    private int findPositionInRanking(Game game, List<Game> ranking) {
        for (int i = 0; i < ranking.size(); i++) {
            if (ranking.get(i).getId().equals(game.getId())) {
                return i + 1;
            }
        }
        return ranking.size() + 1;
    }

    // REST API methods
    public List<Game> getGamesWithMinCorrectWords(String playerAlias, int minCorrectWords) {
        return gameRepo.getGamesByPlayerWithMinCorrectWords(playerAlias, minCorrectWords);
    }

    public void addConfiguration(Configuration configuration) {
        configRepo.save(configuration);
    }
}