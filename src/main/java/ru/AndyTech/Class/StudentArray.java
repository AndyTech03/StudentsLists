package ru.AndyTech.Class;

import ru.AndyTech.Interface.Student;
import ru.AndyTech.Interface.StudentList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StudentArray implements StudentList {
    int ID = 0;
    ArrayList<Student> array = new ArrayList<>();

    @Override
    public List<Student> getAll() {
        return (ArrayList<Student>) array.clone();
    }

    @Override
    public int add(Student value) {
        value.SetID(ID++);
        array.add(value);
        return value.GetID();
    }

    @Override
    public Student getOne(int id) {
        if (id < 0 || id >= array.size())
            return null;
        return array.stream().findFirst(a -> a.id == id);
    }

    @Override
    public void deleteOne(int id) {

    }

    @Override
    public void editOne(int id, Student value) {

    }
}
