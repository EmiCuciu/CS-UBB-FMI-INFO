namespace seminar10;

public class MainClass
{
    public static void Main(string[] args)
    {
        Task task = new Task("1", "Descrierea task-ului");
        Console.WriteLine(task.ToString());

        IContainer stackContainer = new StackContainer(5);
        stackContainer.Add(new Task("1", "Task 1"));
        stackContainer.Add(new Task("2", "Task 2"));
        stackContainer.Add(new Task("3", "Task 3"));
        stackContainer.Add(new Task("4", "Task 4"));
        stackContainer.Add(new Task("5", "Task 5"));

        while (!stackContainer.IsEmpty())
        {
            Task currentTask = stackContainer.Remove();
            Console.WriteLine(currentTask.ToString());

        }
    }
}