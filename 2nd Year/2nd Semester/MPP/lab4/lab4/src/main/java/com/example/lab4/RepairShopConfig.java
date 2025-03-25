package com.example.lab4;

import com.example.lab4.repository.ComputerRepairRequestRepository;
import com.example.lab4.repository.ComputerRepairedFormRepository;
import com.example.lab4.repository.jdbc.ComputerRepairRequestJdbcRepository;
import com.example.lab4.repository.jdbc.ComputerRepairedFormJdbcRepository;
import com.example.lab4.services.ComputerRepairServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class RepairShopConfig {
    @Bean
    Properties getProps() {

        Properties properties = new Properties();
        try {
            System.out.println("Searching for bd.config in: " + new File("bd.config").getAbsolutePath());
            properties.load(new FileReader(new File("bd.config")));
        } catch (IOException e) {
            System.err.println("Cannot find bd.config " + e);
        }

        return properties;
    }

    @Bean
    ComputerRepairRequestRepository requestsRepo() {
        return new ComputerRepairRequestJdbcRepository(getProps());
    }

    @Bean
    ComputerRepairedFormRepository formsRepo() {
        return new ComputerRepairedFormJdbcRepository(getProps());
    }

    @Bean
    ComputerRepairServices services() {
        return new ComputerRepairServices(requestsRepo(), formsRepo());
    }

}
