package com.rest.cfg;

import com.persistence.IConfiguratieRepository;
import com.persistence.IJocRepository;
import com.persistence.IPlayerRepository;
import com.persistence.IPositionRepository;
import com.persistence.repo.ConfiguratieRepository;
import com.persistence.repo.JocRepository;
import com.persistence.repo.PlayerRepository;
import com.persistence.repo.PositionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public IJocRepository jocRepository() {
        return new JocRepository();
    }

    @Bean
    public IPlayerRepository playerRepository() {
        return new PlayerRepository();
    }

    @Bean
    public IPositionRepository positionRepository() {
        return new PositionRepository();
    }

    @Bean
    public IConfiguratieRepository configuratieRepository() {
        return new ConfiguratieRepository();
    }
}