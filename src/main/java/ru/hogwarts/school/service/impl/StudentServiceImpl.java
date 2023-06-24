package ru.hogwarts.school.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(final StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student findStudent(long id) {
        return studentRepository.findById(id).get();
    }

    @Override
    public Student editStudent(long id, Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Collection<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    @Override
    public Collection<Student> getAll() {
        return studentRepository.findAll();
    }

    @Override
    public int size() {
        return getAll().size();
    }

}
