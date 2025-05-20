namespace TriatlonServicess;

public class TriatlonException : Exception
{
    public TriatlonException() : base()
    {
    }

    public TriatlonException(String msg) : base(msg)
    {
    }

    public TriatlonException(string message, Exception innerException)
        : base(message, innerException)
    {
    }

    protected TriatlonException(System.Runtime.Serialization.SerializationInfo info,
        System.Runtime.Serialization.StreamingContext context)
        : base(info, context)
    {
    }
}