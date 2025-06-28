package rest.services;

import domain.Configuratie;
import domain.Incercare;
import domain.Joc;
import network.dto.AttemptDTO;
import network.dto.ConfigurationDTO;
import network.dto.GameDetailsDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import persistence.repository.IRepositories.IRepoConfiguratii;
import persistence.repository.IRepositories.IRepoIncercari;
import persistence.repository.IRepositories.IRepoJocuri;
import services.ServiceException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameRestService {
    private static final Logger logger = LogManager.getLogger();

    private final IRepoJocuri repoJocuri;
    private final IRepoConfiguratii repoConfiguratii;
    private final IRepoIncercari repoIncercari;

    @Autowired
    public GameRestService(IRepoJocuri repoJocuri, IRepoConfiguratii repoConfiguratii, IRepoIncercari repoIncercari) {
        this.repoJocuri = repoJocuri;
        this.repoConfiguratii = repoConfiguratii;
        this.repoIncercari = repoIncercari;
    }

    public GameDetailsDTO getGameDetails(Integer gameId) {
        logger.info("Getting details for game with ID: {}", gameId);

        Joc joc = repoJocuri.findOne(gameId)
                .orElseThrow(() -> new ServiceException("Game not found with id: " + gameId));

        List<Incercare> incercari = repoIncercari.findByJoc(joc);

        GameDetailsDTO dto = new GameDetailsDTO();
        dto.setId(joc.getId());
        dto.setPlayerAlias(joc.getJucator().getnume());
        dto.setConfiguration(joc.getConfiguratie().getCuvinte());
        dto.setScore(joc.getPunctaj());
        dto.setStartTime(joc.getDataInceput());
        dto.setEndTime(joc.getDataSfarsit());
        dto.setStatus(joc.getStatus().toString());

        List<AttemptDTO> attemptDTOs = incercari.stream()
                .map(incercare -> {
                    AttemptDTO attemptDTO = new AttemptDTO();
                    attemptDTO.setPosition1(incercare.getPozitia1());
                    attemptDTO.setPosition2(incercare.getPozitia2());
                    attemptDTO.setAttemptTime(incercare.getDataIncercare());
                    attemptDTO.setPointsEarned(incercare.getPuncteObtinute());
                    attemptDTO.setMatched(incercare.getPotrivire());
                    return attemptDTO;
                })
                .collect(Collectors.toList());

        dto.setAttempts(attemptDTOs);

        return dto;
    }

    public ConfigurationDTO updateConfiguration(Integer configId, ConfigurationDTO configDto) {
        logger.info("Updating configuration with ID: {}", configId);

        Configuratie configuratie = repoConfiguratii.findOne(configId)
                .orElseThrow(() -> new ServiceException("Configuration not found with id: " + configId));

        configuratie.setCuvinte(configDto.getWords());
        repoConfiguratii.update(configuratie);

        ConfigurationDTO result = new ConfigurationDTO();
        result.setId(configuratie.getId());
        result.setWords(configuratie.getCuvinte());

        return result;
    }
}