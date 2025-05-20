package triatlon.persistence;

import triatlon.model.Arbitru;
import triatlon.persistence.IRepository;

public interface IArbitruRepository extends IRepository<Integer, Arbitru> {
    Arbitru findBy(String username, String password);
}
