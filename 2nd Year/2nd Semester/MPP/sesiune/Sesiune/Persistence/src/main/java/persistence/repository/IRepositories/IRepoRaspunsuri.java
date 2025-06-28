package persistence.repository.IRepositories;

import domain.Joc;
import domain.Intrebare;
import domain.RaspunsJucator;
import persistence.repository.IRepository;
import java.util.List;

public interface IRepoRaspunsuri extends IRepository<Integer, RaspunsJucator> {
    List<RaspunsJucator> findByJoc(Joc joc);
    List<RaspunsJucator> findByJocAndIntrebare(Joc joc, Intrebare intrebare);
}