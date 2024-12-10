namespace seminar11;

public class InMemoryRepository<ID, E> : IRepository<ID, E> where E : Entity<ID>
{
    protected IValidator<E> vali;
    protected IDictionary<ID, E> entities = new Dictionary<ID, E>();

    public InMemoryRepository(IValidator<E> vali)
    {
        this.vali = vali;
        
    }

    public E FindOne(ID id)
    {
        if (entities.ContainsKey(id)) return entities[id];
        return default(E);
    }

    public IEnumerable<E> FindAll()
    {
        return entities.Values;
    }

    public E Save(E entity)
    {
        if (entity == null) throw new ArgumentNullException("entity must not be null");
        this.vali.Validate(entity);
        if (this.entities.ContainsKey(entity.ID))
        {
            return entity;
        }

        this.entities[entity.ID] = entity;
        return default(E);
    }

    public E Delete(ID id)
    {
        E entity = this.FindOne(id);
        entities.Remove(id);
        return entity;
    }

    public E Update(E entity)
    {
        if (entity == null) throw new ArgumentNullException("entity must not be null");
        if (FindOne(entity.ID) == null)
        {
            return Save(entity);
        }
        else
        {
            entities[entity.ID] = entity;
            return default(E);
        }
    }
}