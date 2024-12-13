namespace lab11;

public class StrategyTaskRunner:TaskRunner
{
    private Container container;

    public StrategyTaskRunner(Strategy strategy)
    {
        if (strategy == Strategy.Fifo)
        {
            container = new QueueContainer(10);
        }
        else
        {
            container = new StackContainer(10);
        }
    }
    public void ExecuteOneTask()
    {
        container.Remove().Execute();
    }

    public void ExecuteAll()
    {
        var size = container.Size();
        for (var i = 0; i < size; i++)
        {
            container.Remove().Execute();
        }
    }

    public void AddTask(Task task)
    {
        container.Add(task);
    }

    public bool HasTasks()
    {
        return container.Size() < 1;
    }
}