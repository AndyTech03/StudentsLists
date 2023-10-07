package ru.AndyTech.Class;

import ru.AndyTech.Interface.Student;
import ru.AndyTech.Interface.StudentList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
        return array.stream()
                .filter(student -> student.GetID() == id).findFirst()
                .orElse(null);
    }

    @Override
    public void deleteOne(int id) {
        array.removeIf(student -> student.GetID() == id);
    }

    @Override
    public void editOne(int id, Student value) {
        value.SetID(id);
        array = (ArrayList<Student>) array.stream()
                .map(student -> student.GetID() == id ? value : student)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Array: ");
        for (Student student :
                array) {
            result.append(student.toString());
        }
        return result.toString();
    }
}
