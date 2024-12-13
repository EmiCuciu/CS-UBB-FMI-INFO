namespace lab11;

public class TaskContainerFactory:Factory
{

    public Container CreateContainer(Strategy strategy)
    {
        if (strategy == Strategy.Fifo)
        {
            return new QueueContainer(10);
        }
        return new StackContainer(10);
    }
}