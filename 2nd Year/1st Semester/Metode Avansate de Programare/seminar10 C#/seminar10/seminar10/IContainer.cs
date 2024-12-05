namespace seminar10;

public interface IContainer
{
    public IContainer CreateContainer(Strategy strategy)
    {
        switch (strategy)
        {
            case Strategy.LIFO:
                return new StackContainer(10); 
            case Strategy.FIFO:
                return new QueueContainer(10); 
            default:
                throw new ArgumentException("Strategia specificată nu este validă.");
        }
    }
    
    Task Remove();
    void Add(Task task);
    int Size();
    bool IsEmpty();
}