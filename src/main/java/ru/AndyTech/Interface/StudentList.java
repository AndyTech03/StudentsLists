package ru.AndyTech.Interface;

import ru.AndyTech.Class.Student;

import java.util.List;

public interface StudentList {
    int size();
    List<Student> getAll();
    int add(Student value);
    Student getOne(int id);
    void deleteOne(int id);
    void editOne(int id, Student value);
}
