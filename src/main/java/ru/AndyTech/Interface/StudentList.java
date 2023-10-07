package ru.AndyTech.Interface;

import java.util.List;

public interface StudentList {
    List<Student> getAll();
    int add(Student value);
    Student getOne(int id);
    void deleteOne(int id);
    void editOne(int id, Student value);
}
