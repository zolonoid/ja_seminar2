import java.util.Scanner;

/**
 * Напишите метод, который принимает на вход строку (String)
 * и определяет является ли строка палиндромом (возвращает boolean значение).
 */
public class task3
{
    public static void main(String[] args)
    {
        System.out.println("Введите строку...");
        Scanner sc = new Scanner(System.in, System.console().charset());
        String input = sc.nextLine();
        System.out.printf("Строка '%s'%sявляется палиндромом.", input, IsPalindrome(input) ? " " : " не ");
        sc.close();
    }
    
    // Определить является ли строка str палиндромом.
    private static boolean IsPalindrome(String str)
    {
        boolean isPalindrome = false;
        if(str.length() >= 2)
        {
            String rstr = StrReverse(str);
            if(str.equals(rstr))
                isPalindrome = true;
        }
        return isPalindrome;
    }
    
    // Создать из строки str строку с обратным порядком символов.
    private static String StrReverse(String str)
    {
        char[] rstr = new char[str.length()];
        for (int i = 0, j = str.length() - 1; i < str.length(); i++, j--)
            rstr[j] = str.charAt(i);
        return new String(rstr);
    }
}