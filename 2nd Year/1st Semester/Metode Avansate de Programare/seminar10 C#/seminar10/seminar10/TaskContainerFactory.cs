namespace seminar10;

public class TaskContainerFactory : Factory
{
    private static TaskContainerFactory _instance;

    private TaskContainerFactory() { }

    public static TaskContainerFactory GetInstance()
    {
        if (_instance == null)
        {
            _instance = new TaskContainerFactory();
        }
        return _instance;
    }

    public IContainer CreateContainer(Strategy strategy)
    {
        // Assuming StackContainer is a class that implements IContainer
        return new StackContainer(10); // Adjust the capacity as needed
    }
}