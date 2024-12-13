namespace lab11;

public class SortingTask:Task
{
    private Task[] _tasks;
    private Strategy strategy;
    private AbstractSort<Task> sorter;

    public SortingTask(string taskID, string description, Task[] tasks, Strategy strategy) : base(taskID, description)
    {
        _tasks = tasks;
        this.strategy = strategy;
    }

    public override void Execute()
    {
        if (strategy == Strategy.BubbleSort)
        {
            sorter=new BubbleSort();
            sorter.Sort(_tasks);
        }
        else
        {   sorter=new QuickSort(); 
            sorter.Sort(_tasks);
        }

        for (int i = 0; i < _tasks.Length; i++)
        {
            Console.WriteLine(_tasks[i]);
        }
    }
}