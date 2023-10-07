package ru.AndyTech.App;

import ru.AndyTech.Class.StudentArray;
import ru.AndyTech.Interface.Student;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Student student;
        int id;
        StudentArray array = new StudentArray();
        student = new Student(
                "TestName", "TestSecondName",
                "TestLastName", "БTest"
        );
        array.add(student);
        student = new Student(
                "TestName1", "TestSecondName1",
                "TestLastName1", "БTest1"
        );
        array.add(student);
        id = array.add(student);
        array.editOne(id, new Student("Иван", "Иванов", "Иванович", "Б761-2"));
        System.out.println(array);
        array.deleteOne(id);
        System.out.println(array);
    }
}
