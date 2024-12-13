using System.Transactions;

namespace lab11;

public class MessageTask : Task {
    private string message;
    private string from;
    private DateTime date;

    public MessageTask(string from, string message, DateTime date, string taskID, string description):base(taskID, description)
    {
        this.message = message;
        this.from = from;
        this.date = date;
    }
    
    public override void Execute()
    {
        Console.WriteLine(TaskID+" "+Description+" "+from+" "+message+" "+date);
    }

    public string Message
    {
        get { return message; }
        set { message = value; }
    }

    public string From
    {
        get { return from; }
        set { from = value; }
    }

    public DateTime Date
    {
        get { return date; }
        set { date = value; }
    }
}