namespace lab11;

public class StackContainer : Container
{
    private Task[] tasks;
    private int top;

    public StackContainer(int capacity)
    {
        tasks = new Task[capacity];
        top = -1;

    }
    
    public Task Remove()
    {
        if (top > -1)
        {
            Task task = tasks[top];
            tasks[top] = null;
            top--;
            return task;
        }
        throw new InvalidOperationException("Stack container is empty");
    }

    public void Add(Task task)
    {
        if (top < tasks.Length-1)
        {
            top++;
            tasks[top] = task;
        }
        else throw new InvalidOperationException("Stack container is full");
        
        

    }

    public int Size()
    {
        return top + 1;
    }

    public bool IsEmpty()
    {
        return top == -1;
    }
}