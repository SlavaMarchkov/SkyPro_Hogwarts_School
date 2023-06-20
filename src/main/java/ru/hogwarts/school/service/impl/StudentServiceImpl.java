package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Service
public class StudentServiceImpl implements StudentService {

    private final HashMap<Long, Student> students = new HashMap<>();
    private long count = 0;

    @Override
    public Student addStudent(Student student) {
        student.setId(count++);
        students.put(student.getId(), student);
        return student;
    }

    @Override
    public Student findStudent(long id) {
        return students.get(id);
    }

    @Override
    public Student editStudent(long id, Student student) {
        if (!students.containsKey(id)) {
            return null;
        }
        students.put(id, student);
        return student;
    }

    public void deleteStudent(long id) {
        students.remove(id);
    }

    public Collection<Student> filterByAge(int age) {
        ArrayList<Student> filteredStudents = new ArrayList<>();
        for (Student student : students.values()) {
            if (student.getAge() == age) {
                filteredStudents.add(student);
            }
        }
        return filteredStudents;
    }

}
