namespace lab11;

public class PrinterTaskRunner:AbstractTaskRunner
{
    public PrinterTaskRunner(TaskRunner taskRunner) : base(taskRunner)
    {
    }

    public override void ExecuteOneTask() 
    {   base.ExecuteOneTask();
        Console.WriteLine("ExecuteOneTask called at:"+DateTime.Now);
    } 
    public override void ExecuteAll()
    {   base.ExecuteAll();
        Console.WriteLine("ExecuteAll called at:"+DateTime.Now);
    }

    public override void AddTask(Task task)
    {   base.AddTask(task);
        Console.WriteLine("AddTask called at:"+DateTime.Now);
        
    }

    public override bool HasTasks()
    {   
        Console.WriteLine("HasTasks called at:"+DateTime.Now);
        return base.HasTasks();
    }
    
}