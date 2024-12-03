namespace seminar10;

public class MainClass
{
    public static void Main(string[] args)
    {
        Task task = new Task("1", "Descrierea task-ului");
        Console.WriteLine(task.ToString());
    }
}