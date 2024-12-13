namespace lab11;

public abstract class AbstractTaskRunner:TaskRunner
{
    private TaskRunner _taskRunner;

    public AbstractTaskRunner(TaskRunner taskRunner)
    {
        this._taskRunner = taskRunner;
    }
    public virtual void ExecuteOneTask()
    {
        _taskRunner.ExecuteOneTask();
    }

    public virtual void ExecuteAll()
    {
        _taskRunner.ExecuteAll();
    }

    public virtual void AddTask(Task task)
    {
        _taskRunner.AddTask(task);
    }

    public virtual bool HasTasks()
    {
        return _taskRunner.HasTasks();
    }
}