using TriatlonModel;

namespace TriatlonPersistance
{
    public interface IRepository<ID, E> where E : Entity<ID>
    {
        E FindOne(ID id);
        List<E> FindAll();
        void Save(E entity);
        void Delete(ID id);
        void Update(E entity);
    }
}