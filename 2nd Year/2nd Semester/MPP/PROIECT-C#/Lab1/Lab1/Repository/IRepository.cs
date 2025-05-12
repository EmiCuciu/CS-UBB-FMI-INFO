using System.Collections.Generic;
using Lab1.Domain;

namespace Lab1.Repository {
    public interface IRepository<ID, E> where E : Entity<ID> {
        E FindOne(ID id);
        List<E> FindAll();
        void Save(E entity);
        void Delete(ID id);
        void Update(E entity);
    }
}
