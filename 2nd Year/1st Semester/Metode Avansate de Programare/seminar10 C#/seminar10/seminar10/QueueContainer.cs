namespace seminar10;

public class QueueContainer : IContainer
{
    private Task[] _tasks;
    private int _capacity;
    private int _size;
    private int _front;
    private int _rear;

    public QueueContainer(int capacity)
    {
        _capacity = capacity;
        _tasks = new Task[_capacity];
        _size = 0;
        _front = 0;
        _rear = -1;
    }

    public void Add(Task task)
    {
        if (_size == _capacity)
        {
            throw new InvalidOperationException("Queue is full");
        }

        _rear = (_rear + 1) % _capacity;
        _tasks[_rear] = task;
        _size++;
    }

    public bool IsEmpty()
    {
        return _size == 0;
    }

    public Task Remove()
    {
        if (IsEmpty())
        {
            throw new InvalidOperationException("Queue is empty");
        }

        Task task = _tasks[_front];
        _front = (_front + 1) % _capacity;
        _size--;
        return task;
    }

    public int Size()
    {
        return _size;
    }

    public override string ToString()
    {
        string result = "Queue: ";
        for (int i = 0; i < _size; i++)
        {
            int index = (_front + i) % _capacity;
            result += _tasks[index] + " ";
        }
        return result;
    }
}