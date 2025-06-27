package com.persistence;

import com.model.Game;

import java.util.List;

public interface IGameRepository extends IRepository<Integer, Game> {

    List<Game> getGamesByPlayerWithMinCorrectWords(String playerAlias, int minCorrectWords);
    List<Game> getAllGamesForRanking();
}
