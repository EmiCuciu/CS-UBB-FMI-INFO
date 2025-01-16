namespace LAB10_NBA.Utils;

public class RandomUtils
{
    private static Random _random = new Random();
    public static List<T> GetRandomItems<T> (List<T> list, int count){
        return list.OrderBy(x => _random.Next()).Take(count).ToList();
    }

    public static int GetRandomNumber(int min, int max){
        return _random.Next(min, max+1);   
    }
}