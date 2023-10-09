package ru.AndyTech.Class;

public class Student {
    int id;
    String firstName;
    String middleName;
    String lastName;
    String groupCode;

    public Student(int id, String firstName, String middleName, String lastName, String groupCode) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.groupCode = groupCode;
    }

    public Student(String firstName, String middleName, String lastName, String groupCode) {
        id = -1;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.groupCode = groupCode;
    }

    public Student(int id, Student value) {
        this.id = id;
        this.firstName = value.firstName;
        this.middleName = value.middleName;
        this.lastName = value.lastName;
        this.groupCode = value.groupCode;
    }

    public int getID(){
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (id != student.id) return false;
        if (!firstName.equals(student.firstName)) return false;
        if (!middleName.equals(student.middleName)) return false;
        if (!lastName.equals(student.lastName)) return false;
        return groupCode.equals(student.groupCode);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Student {" +
                "id=" + id +
                ", name='" + firstName +
                " " + middleName +
                " " + lastName + "'" +
                ", groupCode='" + groupCode + "'" +
                '}';
    }
}
