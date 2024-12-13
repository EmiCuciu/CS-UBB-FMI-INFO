using System.Security.AccessControl;

namespace lab11;

public class DelayTaskRunner:AbstractTaskRunner
{
    public DelayTaskRunner(TaskRunner taskRunner) : base(taskRunner)
    {
    }

    public override void ExecuteOneTask()
    { 
        Thread.Sleep(3000);
        base.ExecuteOneTask();
        
        
    }

    public override void ExecuteAll()
    {
        Thread.Sleep(3000);
        base.ExecuteAll();
    }

    public override void AddTask(Task task)
    {
        Thread.Sleep(3000);
        base.AddTask(task);
    }

    public override bool HasTasks()
    {
        Thread.Sleep(3000);
        return base.HasTasks();
    }
}