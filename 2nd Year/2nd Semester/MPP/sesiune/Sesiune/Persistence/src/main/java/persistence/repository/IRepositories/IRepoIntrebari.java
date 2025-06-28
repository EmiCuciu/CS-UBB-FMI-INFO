package persistence.repository.IRepositories;

import domain.Intrebare;
import persistence.repository.IRepository;
import java.util.List;

public interface IRepoIntrebari extends IRepository<Integer, Intrebare> {
    List<Intrebare> findByNivel(int nivel);
}