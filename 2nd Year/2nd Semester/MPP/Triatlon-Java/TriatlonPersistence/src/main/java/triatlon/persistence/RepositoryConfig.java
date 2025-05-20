package triatlon.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class RepositoryConfig {

    @Bean
    @Qualifier("dbProperties")
    public Properties dbProperties() {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("D:/GithubRepositories/mpp-proiect-java-EmiCuciu/Laborator/TriatlonServer/src/main/resources/chatserver.properties"));
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load properties file", e);
        }
    }

    @Bean
    @Primary
    public IArbitruRepository arbitruRepositoryBean(@Qualifier("dbProperties") Properties properties) {
        return new ArbitruRepository(properties);
    }

    @Bean
    @Primary
    public IProbaRepository probaRepository(@Qualifier("dbProperties") Properties properties, IArbitruRepository arbitruRepository) {
        return new ProbaRepository(properties, arbitruRepository);
    }
}