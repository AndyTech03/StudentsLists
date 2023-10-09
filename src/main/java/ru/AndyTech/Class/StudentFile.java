package ru.AndyTech.Class;

import ru.AndyTech.Interface.StudentList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.function.Function;


public class StudentFile implements StudentList
{
    Path filePath ;

    public StudentFile() {
        try {
            filePath = Path.of(System.getProperty("user.dir")+"/data.sf");
            if (Files.notExists(filePath))
                Files.createFile(filePath);
            System.out.println(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Object fileReading(Function<FileInputStream, Object> reader) {
        FileInputStream stream = null;
        Object data;

        try {
            if (Files.notExists(filePath))
                Files.createFile(filePath);
            stream = new FileInputStream(filePath.toString());
            data = reader.apply(stream);
            stream.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    private Boolean fileWriting(Function<FileOutputStream, Boolean> writer) {
        FileOutputStream stream = null;
        Boolean status = false;
        try {
            if (Files.notExists(filePath))
                Files.createFile(filePath);
            stream = new FileOutputStream(filePath.toString());
            status = writer.apply(stream);
            stream.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return status;
    }

    private byte toUnsignedByte(int signedByte) {
        return (byte) (signedByte >= 0 ? signedByte : 256 + signedByte);
    }
    private int toSignedByte(int unsignedByte) {
        return unsignedByte <= 127 ? unsignedByte : unsignedByte - 256;
    }

    private String readByteString(FileInputStream stream) throws IOException {
        int len = toUnsignedByte(stream.read());
        return new String(stream.readNBytes(len), StandardCharsets.UTF_8);
    }

    private byte[] convertStringToByte(String value){
        byte[] data = value.getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[data.length + 1];
        result[0] = (byte)toSignedByte(data.length);
        System.arraycopy(data, 0, result, 1, data.length);
        return result;
    }

    @Override
    public int size() {
        int signedSize = (int) fileReading(stream -> {
            try {
                return stream.read();
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
        });
        return toUnsignedByte(signedSize);
    }

    @Override
    public List<Student> getAll() {
        ArrayList<Student> data = (ArrayList<Student>) fileReading(stream -> {
            int len;
            int nextID;
            int count;
            try {
                count = toUnsignedByte(stream.read());
                nextID = toUnsignedByte(stream.read());
                ArrayList<Student> result = new ArrayList<>(count);
                for (int i = 0; i < count; i++){
                    len = toUnsignedByte(stream.read());
                    result.add(i, new Student (
                            toUnsignedByte(stream.read()),
                            readByteString(stream), readByteString(stream), readByteString(stream),
                            readByteString(stream)
                    ));
                }
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
        return data;
    }

    @Override
    public int add(Student value) {
        byte[] data = (byte[]) fileReading(stream -> {
            try {
                return stream.readAllBytes();
            } catch (IOException e) {
                return 0;
            }
        });
        int count = toUnsignedByte(data[0]) + 1;
        data[0] = (byte) toSignedByte(count);
        int id = toUnsignedByte(data[1]);
        data[1] = (byte) toSignedByte(id + 1);

        byte[] first = convertStringToByte(value.firstName);
        byte[] middle = convertStringToByte(value.middleName);
        byte[] last = convertStringToByte(value.lastName);
        byte[] group = convertStringToByte(value.groupCode);

        int size = 1 + first.length + middle.length + last.length + group.length;
        int index = 0;
        byte[] result = new byte[data.length + size + 1];

        System.arraycopy(data, 0, result, 0, data.length);      // place old
        index += data.length;
        result[index++] = toUnsignedByte(size);                              // store size
        result[index++] = toUnsignedByte(id);                                // store id
        System.arraycopy(first, 0, result, index, first.length);       // place first
        index += first.length;
        System.arraycopy(middle, 0, result, index, middle.length);     // place middle
        index += middle.length;
        System.arraycopy(last, 0, result, index, last.length);         // place last
        index += last.length;
        System.arraycopy(group, 0, result, index, group.length);         // place last

        if (fileWriting(stream -> {
            try {
                stream.write(result);
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }))
            return id;
        return -1;
    }

    @Override
    public Student getOne(int id) {
        return (Student) fileReading(stream -> {
            try {
                int count = toUnsignedByte(stream.read());
                int nextID = toUnsignedByte(stream.read());
                int size;
                for (int i = 0; i < count; i++){
                    size = toUnsignedByte(stream.read());
                    if (id == toUnsignedByte(stream.read())){
                        return new Student (
                                id,
                                readByteString(stream), readByteString(stream), readByteString(stream),
                                readByteString(stream)
                        );
                    }
                    else
                        stream.skip(size-1);
                }
                return null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void deleteOne(int id) {
        byte[] data = (byte[]) fileReading(stream -> {
            try {
                return stream.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        int count = toUnsignedByte(data[0]);
        int index = 2;
        int size = 0;
        boolean founded = false;
        for (int i = 0; i < count; i++){
            size = toUnsignedByte(data[index]);
            if (id == toUnsignedByte(data[index + 1])){
                founded = true;
                break;
            }
            else
                index += size+1;
        }
        if (founded == false)
            return;

        byte[] result = new byte[data.length - size - 1];
        System.arraycopy(data, 0, result, 0, index);
        System.arraycopy(data, index + size + 1, result, index, result.length - index);
        result[0] = (byte) toSignedByte(count-1);
        fileWriting(stream -> {
            try {
                stream.write(result);
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void editOne(int id, Student value) {
        deleteOne(id);
        byte[] data = (byte[]) fileReading(stream -> {
            try {
                return stream.readAllBytes();
            } catch (IOException e) {
                return 0;
            }
        });
        int count = toUnsignedByte(data[0]) + 1;
        data[0] = (byte) toSignedByte(count);

        byte[] first = convertStringToByte(value.firstName);
        byte[] middle = convertStringToByte(value.middleName);
        byte[] last = convertStringToByte(value.lastName);
        byte[] group = convertStringToByte(value.groupCode);

        int size = 1 + first.length + middle.length + last.length + group.length;
        int index = 0;
        byte[] result = new byte[data.length + size + 1];

        System.arraycopy(data, 0, result, 0, data.length);      // place old
        index += data.length;
        result[index++] = toUnsignedByte(size);                              // store size
        result[index++] = toUnsignedByte(id);                                // store id
        System.arraycopy(first, 0, result, index, first.length);       // place first
        index += first.length;
        System.arraycopy(middle, 0, result, index, middle.length);     // place middle
        index += middle.length;
        System.arraycopy(last, 0, result, index, last.length);         // place last
        index += last.length;
        System.arraycopy(group, 0, result, index, group.length);         // place last

        fileWriting(stream -> {
            try {
                stream.write(result);
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("File {\n");
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
