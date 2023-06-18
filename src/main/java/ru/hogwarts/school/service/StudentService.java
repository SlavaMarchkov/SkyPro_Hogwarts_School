package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;

@Service
public class StudentService {

    private final HashMap<Long, Student> students = new HashMap<>();
    private long count = 0;

    public Student create(Student student) {
        student.setId(count++);
        students.put(student.getId(), student);
        return student;
    }

    public Student read(long id) {
        return students.get(id);
    }

    public Student update(Student student) {
        if (!students.containsKey(student.getId())) {
            return null;
        }
        students.put(student.getId(), student);
        return student;
    }

    public Student delete(long id) {
        return students.remove(id);
    }

}
