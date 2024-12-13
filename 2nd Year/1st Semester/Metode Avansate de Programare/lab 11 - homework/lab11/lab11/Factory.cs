namespace lab11;

public interface Factory
{
    Container CreateContainer(Strategy strategy);
}