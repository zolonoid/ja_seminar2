import java.io.IOException;
import java.util.Random;
import java.util.logging.*;

/**
 * Реализация алгоритма сортировки пузырьком числового массива
 * с записью результата после каждой итерации в лог-файл.
 */
public class task1
{
    public static void main(String[] args) throws SecurityException, IOException
    {
        int[] arr = CreateRandomArray(1, 10, 5);
        String out = String.format("Сортировка случайного массива: %s -> ", ArrayToString(arr));
        SortArrayWithLog(arr);
        System.out.printf("%s%s\n", out, ArrayToString(arr));
        System.out.println("Результат после каждой итерации записан в лог-файл task1.log");
    }
    
    // Создать массив размером size, заполненный случайными числами в диапазоне от min до max.
    private static int[] CreateRandomArray(int min, int max, int size)
    {
        var randArray = new int[size];
        var rand = new Random();
        for(int i = 0; i < size; i++)
            randArray[i] = rand.nextInt(min,max);
        return randArray;
    }

    // Выполнить сортировку пузырьком массива arr с записью в лог-файл.
    private static void SortArrayWithLog(int[] arr) throws SecurityException, IOException
    {
        for (int j = 1; j < arr.length; j++)
        {
            Boolean f = false;
            for (int i = 0; i < arr.length - j; i++)
            {
                if(arr[i] > arr[i + 1])
                {
                    int t = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = t;
                    f = true;
                }
                WriteLog(ArrayToString(arr));
            }
            if(!f) break;
        }
    }

    // Вывести в консоль строковое представление массива arr.
    private static String ArrayToString(int[] arr)
    {
        var builder = new StringBuilder("[");
        for(int i = 0; i < arr.length; i++)
            builder.append(String.format("%d%s",arr[i],i < arr.length - 1 ? "," : ""));
        builder.append("]");
        return builder.toString();
    }

    // Заисать в лог файл строку log.
    private static void WriteLog(String log) throws SecurityException, IOException
    {
        Logger logger = Logger.getLogger(task1.class.getName());
        logger.setLevel(Level.INFO);
        FileHandler fh = new FileHandler("task1.log", true);
        logger.addHandler(fh);
        fh.setFormatter(new SimpleFormatter());
        logger.setUseParentHandlers(false);
        logger.log(Level.INFO, log);
        fh.close();
    }
}