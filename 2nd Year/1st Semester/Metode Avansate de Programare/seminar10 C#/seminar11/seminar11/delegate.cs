namespace seminar11;

public delegate E CreateEntity<E>(string line);

abstract class InFileRepository<ID, E> : InMemoryRepository<ID, E> where E : Entity<ID>
{
    protected string fileName;
    protected CreateEntity<E> createEntity;

    public InFileRepository(IValidator<E> vali, String fileName, CreateEntity<E> createEntity) : base(vali)
    {
        this.fileName = fileName;
        this.createEntity = createEntity;
        if (createEntity != null)
            loadFromFile();
    }

    protected virtual void loadFromFile()
    {
        List<E> list = new List<E>();
        using (StreamReader sr = new StreamReader(fileName))
        {
            string s;
            while ((s = sr.ReadLine()) != null)
            {
                E entity = createEntity(s);
                list.Add(entity);
            }
        }

        foreach (var x in list)
            entities[x.ID] = x;
    }
}