namespace lab11;

public class QuickSort:AbstractSort<Task>
{
    public override void Sort(Task[] array)
    {
        QuickSortInternal(array, 0, array.Length - 1);
    }

    private void QuickSortInternal(Task[] array, int low, int high)
    {
        if (low < high)
        {
            int pivotIndex = Partition(array, low, high);
            
            QuickSortInternal(array, low, pivotIndex - 1);
            QuickSortInternal(array, pivotIndex + 1, high);
        }
    }

    private int Partition(Task[] array, int low, int high)
    {
        Task pivot = array[high];
        int i = low - 1; 

        for (int j = low; j < high; j++)
        {
            if (!array[j].IsGreaterThan(pivot)) 
            {
                i++;
                Swap(array, i, j);
            }
        }
        
        Swap(array, i + 1, high);
        return i + 1;
    }

    private void Swap(Task[] array, int a, int b)
    {
        (array[a], array[b]) = (array[b], array[a]);
    }
}