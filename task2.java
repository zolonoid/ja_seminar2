import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.*;

/**
 * task2
 */
public class task2
{
    public static void main(String[] args)
    {
        Scanner sc = null;
        try
        {
            var students = StudentCollection.Load("task2.json");
            while(true)
            {
                System.out.println(students.toString());
                System.out.println("Чтобы добавить студента введите строку в формате: фамилия оценка предмет");
                System.out.println("Чтобы выйти нажмите Enter");
                sc = new Scanner(System.in, System.console().charset());
                String[] input = sc.nextLine().split(" ");
                if(input.length != 3) break;
                students.Add(new Student(input[0], input[1], input[2]));
                students.Save();
            }
        }
        catch (Exception e)
        {
            System.out.printf("Ошибка: %s", e.getMessage());
        }
        finally
        {
            if(sc != null)
                sc.close();

        }
    }
}

// Класс содержит информацию о студенте.
class Student
{
    private static Pattern pattern = Pattern.compile(
        "\\{\"фамилия\":\"([а-яА-Я]+)\",\"оценка\":\"(\\d+)\",\"предмет\":\"([а-яА-Я]+)\"\\}"
    );
    private String _surname;
    private String _score;
    private String _subject;
    
    Student(String surname, String score, String subject)
    {
        _surname = surname;
        _score = score;
        _subject = subject;
    }

    // Конструктор десериализации json строки в объект Student.
    Student(String json) throws Exception
    {
        Matcher matcher = pattern.matcher(json);
        if(!matcher.matches())
            throw new Exception("Неверный формат данных студента");
        _surname = matcher.group(1);
        _score = matcher.group(2);
        _subject = matcher.group(3);
    }

    public String getSurname()
    {
        return _surname;
    }

    public String getScore()
    {
        return _score;
    }

    public String getSubject()
    {
        return _subject;
    }

    // Сериализация объекта Student в json строку.
    public String toJsonString()
    {
        return String.format("{\"фамилия\":\"%s\",\"оценка\":\"%s\",\"предмет\":\"%s\"}",
                             _surname, _score, _subject);
    }
    
    @Override
    public String toString()
    {
        
        return String.format("Студент %s получил %s по предмету %s",
                             _surname, _score, _subject);
    }
}


// Класс представляющий список студентов.
class StudentCollection implements Iterable<Student>
{
    private static Pattern pattern = Pattern.compile(
        "\\[((?:\\{\"фамилия\":\"[а-яА-Я]+\",\"оценка\":\"\\d+\",\"предмет\":\"[а-яА-Я]+\"\\},?)*)\\]"
    );
    private Path _file;
    private ArrayList<Student> _students;
    
    public static StudentCollection Load(String file) throws Exception
    {
        Path path = Paths.get(file);
        if(!Files.exists(path))
            return new StudentCollection("[]", path);
        byte[] bjson = Files.readAllBytes(path);
        String sjson = new String(bjson, StandardCharsets.UTF_8);
        return new StudentCollection(sjson, path);
    }
    
    // Создание списка студентов из json строки.
    private StudentCollection(String json, Path file) throws Exception
    {
        _file = file;
        _students = new ArrayList<Student>();
        Matcher matcher = pattern.matcher(json);
        if(!matcher.matches())
            throw new Exception("Неверный формат списка студентов");
        if(matcher.group(1) == "")
            return;
        String[] students = matcher.group(1).split("},");
        for (String student : students)
            _students.add(new Student(student + (student.indexOf('}') < 0 ? "}" : "")));
    }

    public void Add(Student student)
    {
        _students.add(student);
    }

    // Сериализация списка студентов в json строку.
    public String toJsonString()
    {
        var str = new StringBuilder("[");
        for (int i = 0; i < _students.size(); i++)
        {
            Student student = _students.get(i);
            str.append(student.toJsonString());
            if(i < _students.size() - 1) str.append(",");
        }
        str.append("]");
        return str.toString();
    }
    
    @Override
    public String toString()
    {
        var str = new StringBuilder();
        for (int i = 0; i < _students.size(); i++)
        {
            Student student = _students.get(i);
            str.append(student.toString());
            if(i < _students.size() - 1) str.append("\n");
        }
        return str.toString();
    }

    // Сохранить список студентов в json файл.
    public void Save() throws IOException
    {
        String sjson = toJsonString();
        byte[] bjson = sjson.getBytes(StandardCharsets.UTF_8);
        Files.write(_file, bjson);
    }
    
    @Override
    public Iterator<Student> iterator() { return new Iter(); }

    private class Iter implements Iterator<Student>
    {
        private int index = 0;
        
        @Override
        public boolean hasNext() { return index < _students.size(); }

        @Override
        public Student next() { return _students.get(index++); }
    }
}