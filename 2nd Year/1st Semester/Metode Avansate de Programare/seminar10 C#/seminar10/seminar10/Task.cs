namespace seminar10;

public class Task
{
    public string Id { get; set; }
    public string Description { get; set; }

    public Task(string id, string description)
    {
        Id = id;
        Description = description;
    }

    public override string ToString()
    {
        return $"Task ID: {Id}, Description: {Description}";
    }
}