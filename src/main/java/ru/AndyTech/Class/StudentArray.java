package ru.AndyTech.Class;

import ru.AndyTech.Interface.StudentList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentArray implements StudentList {
    int ID = 0;
    ArrayList<Student> array = new ArrayList<>();

    @Override
    public int size() {
        return array.size();
    }

    @Override
    public List<Student> getAll() {
        return (ArrayList<Student>) array.clone();
    }

    @Override
    public int add(Student value) {
        Student item = new Student(ID++, value);
        array.add(item);
        return item.GetID();
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
        Student item = new Student(id, value);
        array = (ArrayList<Student>) array.stream()
                .map(student -> student.GetID() == id ? item : student)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Array {\n");
        for (Student student :
                array) {
            result.append("\t");
            result.append(student.toString());
            result.append(",\n");
        }
        result.delete(result.length() - 2, result.length());
        result.append("\n}");
        return result.toString();
    }
}
