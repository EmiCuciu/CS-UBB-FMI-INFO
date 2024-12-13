namespace lab11;

public class QueueContainer : Container
{
    private Task[] tasks;
    private int size;

    public QueueContainer(int capacity)
    {
        tasks = new Task[capacity];
        size = -1;
    }

    public Task Remove()
    {
        if (size > -1)
        {
            Task removed = tasks[0];
            for (int i = 0; i < size; i++)
            {
                tasks[i] = tasks[i + 1];
            }

            return removed;
        }

        throw new InvalidOperationException("Queue container is empty");
    }


    public void Add(Task task)
    {
        if (size < tasks.Length - 1)
        {
            size++;
            tasks[size] = task;
        }
        else
        {
            throw new InvalidOperationException("Queue container is full");
        }
    }

    public int Size()
    {
        return size + 1;
    }

    public bool IsEmpty()
    {
        return size == -1;
    }
}