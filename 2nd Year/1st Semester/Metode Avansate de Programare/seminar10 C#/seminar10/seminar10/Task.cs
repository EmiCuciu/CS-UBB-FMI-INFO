namespace seminar10;

public class Task
{
    // Atributele clasei
    private string taskID;
    private string descriere;
 
    // Constructor cu parametri
    public Task(string taskID, string descriere)
    {
        this.taskID = taskID;
        this.descriere = descriere;
    }
 
    // Proprietăți (set/get)
    public string TaskID
    {
        get { return taskID; }
        set { taskID = value; }
    }
 
    public string Descriere
    {
        get { return descriere; }
        set { descriere = value; }
    }
 
    // // Metoda abstractă execute()
    // public abstract void Execute();
    //
    // Suprascrierea metodei ToString()
    public override string ToString()
    {
        return $"TaskID: {taskID}, Descriere: {descriere}";
    }
 
    // Suprascrierea metodelor Equals și GetHashCode
    public override bool Equals(object obj)
    {
        if (obj is Task otherTask)
        {
            return taskID == otherTask.taskID &&
                   descriere == otherTask.descriere;
        }
        return false;
    }
 
    public override int GetHashCode()
    {
        return HashCode.Combine(taskID, descriere);
    }
}