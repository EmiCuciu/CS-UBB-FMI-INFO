package persistence.repository.IRepositories;

import domain.Cuvant;
import persistence.repository.IRepository;

import java.util.List;
import java.util.Optional;

public interface IRepoCuvinte extends IRepository<Integer, Cuvant> {
    Optional<Cuvant> findByText(String text);
    List<Cuvant> getRandomCuvinte(int count);
}