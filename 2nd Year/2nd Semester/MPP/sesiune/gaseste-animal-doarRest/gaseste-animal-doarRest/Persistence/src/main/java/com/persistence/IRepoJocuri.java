package persistence.repository.IRepositories;

import domain.Joc;
import network.dto.JocNeghicitDTO;
import persistence.repository.IRepository;

import java.util.List;

public interface IRepoJocuri extends IRepository<Integer, Joc> {
    List<JocNeghicitDTO> findJocuriNeghicite(int id);
}
