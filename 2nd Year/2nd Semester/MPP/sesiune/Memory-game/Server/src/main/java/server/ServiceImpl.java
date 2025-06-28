package server;

//import domain.Configuratie;
//import domain.Incercare;
//import domain.Joc;
import domain.Jucator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import persistence.repository.IRepositories.IRepoConfiguratii;
//import persistence.repository.IRepositories.IRepoIncercari;
//import persistence.repository.IRepositories.IRepoJocuri;
import persistence.repository.IRepositories.IRepoJucatori;
import services.IObserver;
import services.IServices;
import services.ServiceException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ServiceImpl implements IServices {

    //private IRepoConfiguratii repoConfiguratii;
    //private IRepoJocuri repoJocuri;
    private IRepoJucatori repoJucatori;
    //private IRepoIncercari repoIncercari;


    private Map<String, IObserver> loggedJucatori;

    private static Logger logger = LogManager.getLogger(ServiceImpl.class);

    // constructorul asta e doar de proba...
    public ServiceImpl(IRepoJucatori jucatoriRepo) {
        repoJucatori = jucatoriRepo;
    }

//    public ServiceImpl(IRepoConfiguratii repoConfiguratii, IRepoJocuri repoJocuri, IRepoJucatori repoJucatori, IRepoIncercari repoIncercari) {
//        this.repoConfiguratii = repoConfiguratii;
//        this.repoJocuri = repoJocuri;
//        this.repoJucatori = repoJucatori;
//        this.repoIncercari = repoIncercari;
//        this.loggedJucatori = new HashMap<>();
//    }

    @Override
    public void logIn(Jucator jucator, IObserver observer) {
        Optional<Jucator> jucatorr = repoJucatori.findByNume(jucator.getnume());
        if (jucatorr.isPresent()) {
            if(loggedJucatori.containsKey(jucator.getnume())) {
                throw new ServiceException("Jucatorul este deja conectat!");
            }
            loggedJucatori.put(jucator.getnume(), observer);
        }
        else {
            throw new ServiceException("Jucatorul nu a fost gasit!");
        }
    }

    @Override
    public void logOut(Jucator jucator, IObserver observer) {

    }

}
