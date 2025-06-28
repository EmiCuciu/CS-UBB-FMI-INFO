package rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import persistence.repository.IRepositories.IRepoIntrebari;
import persistence.repository.IRepositories.IRepoJocuri;
import persistence.repository.IRepositories.IRepoJucatori;
import persistence.repository.IRepositories.IRepoRaspunsuri;
import persistence.repository.RepoIntrebariHibernate;
import persistence.repository.RepoJocuriHibernate;
import persistence.repository.RepoJucatoriHibernate;
import persistence.repository.RepoRaspunsuriHibernate;

@Configuration
public class RepositoryConfig {
    
    @Bean
    public IRepoJucatori jucatoriRepository() {
        return new RepoJucatoriHibernate();
    }
    
    @Bean
    public IRepoIntrebari intrebariRepository() {
        return new RepoIntrebariHibernate();
    }
    
    @Bean
    public IRepoJocuri jocuriRepository() {
        return new RepoJocuriHibernate();
    }
    
    @Bean
    public IRepoRaspunsuri raspunsuriRepository() {
        return new RepoRaspunsuriHibernate();
    }
}