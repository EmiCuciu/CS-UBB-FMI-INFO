using lab11;
using Task = lab11.Task;

var messageTask=new MessageTask("A", "Salut",DateTime.Now,"9","m1");
var messageTask2=new MessageTask("B", "Salutare",DateTime.Now,"2","m2");
var messageTask3=new MessageTask("C", "Salutare",DateTime.Now,"1","m3");
var messageTask4=new MessageTask("D", "Salutare",DateTime.Now,"4","m4");
var messageTask5=new MessageTask("E", "Salutare",DateTime.Now,"5","m5");

Task[] taskStatic=new Task[]{messageTask,messageTask2,messageTask3,messageTask4,messageTask5};
SortingTask sortingTask = new SortingTask("10","Sorting a task vector",taskStatic, Strategy.QuickSort);
sortingTask.Execute();


var factory=new TaskContainerFactory();
var stackContainer = factory.CreateContainer(Strategy.Fifo);
stackContainer.Add(messageTask);
stackContainer.Add(messageTask2);
stackContainer.Add(messageTask3);
stackContainer.Add(messageTask4);
stackContainer.Add(messageTask5);


var mainTaskRunner = new StrategyTaskRunner(Strategy.Fifo);
var printerTaskRunner  = new PrinterTaskRunner(mainTaskRunner);
var taskRunner = new DelayTaskRunner(printerTaskRunner);
taskRunner.AddTask(messageTask);
taskRunner.AddTask(messageTask2);
taskRunner.AddTask(messageTask3);
taskRunner.AddTask(messageTask4);
taskRunner.AddTask(messageTask5);



for (int i = 0; i < 5; i++)
{
    taskRunner.ExecuteOneTask();
}