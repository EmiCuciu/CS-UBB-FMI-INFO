package persistence.repository.IRepositories;

import domain.Joc;
import domain.Jucator;
import persistence.repository.IRepository;

import java.util.List;

public interface IRepoJocuri extends IRepository<Integer, Joc> {
    List<Joc> findByJucator(Jucator jucator);
    List<Joc> findAllFinishedGames();
}