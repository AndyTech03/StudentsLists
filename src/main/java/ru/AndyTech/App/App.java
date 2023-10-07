package ru.AndyTech.App;

import ru.AndyTech.Class.StudentArray;
import ru.AndyTech.Interface.Student;

public class App {
    public static void main(String[] args) {
        Student student;
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
        array.getAll();
    }
}
