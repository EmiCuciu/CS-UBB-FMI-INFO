package persistence.repository.IRepositories;

import domain.Incercare;
import domain.Joc;
import persistence.repository.IRepository;

import java.util.List;

public interface IRepoIncercari extends IRepository<Integer, Incercare> {
    List<Incercare> findByJoc(Joc joc);
    long countByJoc(Joc joc);
}