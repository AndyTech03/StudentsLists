package ru.AndyTech.Class;

import ru.AndyTech.Interface.StudentList;

import javax.management.RuntimeErrorException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

public class StudentDatabase implements StudentList {
    static Connection conn = null;
    public StudentDatabase(){
        if (conn == null)
            try {
                Connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    public static void Connect() throws Exception {
        if (conn != null)
            throw new RuntimeErrorException(null, "Connection not closed!");

        String url = "jdbc:postgresql://localhost:5432/Learning";
        String user = "postgres";
        String password = "123";
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection(url, user, password);

        if (conn == null)
            throw new Exception("Cant connect to DB!");
        else
            System.out.println("Connected!");
    }

    private Object executeQuery(Function<Statement, Object> query) {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            Object result = query.apply(statement);
            statement.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public int size() {
            return (int) executeQuery(statement -> {
                try {
                    ResultSet set = statement.executeQuery("select count(*) from student;");
                    if (set.next())
                        return set.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return 0;
            });
    }

    @Override
    public List<Student> getAll() {
        return (ArrayList<Student>) executeQuery(statement -> {
            ArrayList<Student> result = new ArrayList<>();
            try {
                ResultSet set = statement.executeQuery("select * from student;");
                while (set.next()){
                    result.add(new Student(
                            set.getInt(1), set.getString(2), set.getString(3),
                            set.getString(4), set.getString(5)
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        });
    }

    @Override
    public int add(Student value) {
        return (int) executeQuery(statement -> {
            try {
                statement.executeUpdate(
                        "insert into student(\"firstName\", \"middleName\", \"lastName\", \"group\") " +
                                "values('"+value.firstName+"', '"+value.middleName+"', " +
                                "'"+value.lastName+"', '"+value.groupCode+"');",
                        Statement.RETURN_GENERATED_KEYS
                );
                ResultSet set = statement.getGeneratedKeys();
                if (set.next()){
                    return set.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        });
    }

    @Override
    public Student getOne(int id) {
        return (Student) executeQuery(statement -> {
            try {
                ResultSet set = statement.executeQuery("select * from student where id = "+id+";");
                if (set.next()){
                    return new Student(
                            set.getInt(1), set.getString(2), set.getString(3),
                            set.getString(4), set.getString(5)
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public void deleteOne(int id) {
        executeQuery(statement -> {
            try {
                statement.execute("delete from student where id = "+id+";");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public void editOne(int id, Student value) {
        executeQuery(statement -> {
            try {
                statement.executeUpdate(
                        "update student set " +
                                "\"firstName\" = '"+value.firstName+"', " +
                                "\"middleName\" = '"+value.middleName+"', " +
                                "\"lastName\" = '"+value.lastName+"', " +
                                "\"group\" = '"+value.groupCode+"' " +
                                "where id = "+id+";"
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Database {\n");
        for (Student student :
                getAll()) {
            result.append("\t");
            result.append(student.toString());
            result.append(",\n");
        }
        result.delete(result.length() - 2, result.length());
        result.append("\n}");
        return result.toString();
    }
}
