package ru.AndyTech.App;

import ru.AndyTech.Class.StudentArray;
import ru.AndyTech.Class.StudentDatabase;
import ru.AndyTech.Class.StudentFile;
import ru.AndyTech.Class.Student;
import ru.AndyTech.Interface.StudentList;

public class App {
    public static void main(String[] args) {
        StudentList list;
        list = new StudentArray();
        testList(list);

        list = new StudentFile();
        testList(list);

        list = new StudentDatabase();
        testList(list);
    }

    private static void testList(StudentList list)
    {
        Student student;
        int id;
        student = new Student(
                "TestName", "TestSecondName",
                "TestLastName", "БTest"
        );
        list.add(student);         // 0
        student = new Student(
                "TestName1", "TestSecondName1",
                "TestLastName1", "БTest1"
        );
        id = list.add(student);    // 1
        list.add(student);         // 2
        System.out.println(list);

        list.editOne(id, new Student("Иван", "Иванов", "Иванович", "Б761-2"));
        System.out.println(list);

        list.deleteOne(id);
        System.out.println(list);

        for (Student item:
                list.getAll()) {
            System.out.println(item.toString());
        }
    }
}
