package ru.AndyTech.Class;

import ru.AndyTech.Interface.Student;
import ru.AndyTech.Interface.StudentList;

import java.util.List;

public class StudentDatabase implements StudentList {
    @Override
    public List<Student> getAll() {
        return null;
    }

    @Override
    public int add(Student value) {
        return -1;
    }

    @Override
    public Student getOne(int id) {
        return null;
    }

    @Override
    public void deleteOne(int id) {

    }

    @Override
    public void editOne(int id, Student value) {

    }
}
