namespace seminar10;

public class StackContainer : IContainer
{
    private Task[] tasks;
    private int top;

    public StackContainer(int capacity)
    {
        tasks = new Task[capacity];
        top = -1;
    }

    public void Add(Task task)
    {
        if (top < tasks.Length - 1)
        {
            tasks[++top] = task;
        }
        else
        {
            throw new InvalidOperationException("Stack is full");
        }
    }

    public bool IsEmpty()
    {
        return top == -1;
    }

    public Task Remove()
    {
        if (IsEmpty())
        {
            throw new InvalidOperationException("Stack is empty");
        }
        return tasks[top--];
    }

    public int Size()
    {
        return top + 1;
    }
}