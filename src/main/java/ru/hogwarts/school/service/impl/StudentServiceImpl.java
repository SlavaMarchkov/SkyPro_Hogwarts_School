package ru.hogwarts.school.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final StudentMapper studentMapper;
    private final FacultyMapper facultyMapper;

    @Autowired
    public StudentServiceImpl(final StudentRepository studentRepository,
                              final FacultyRepository facultyRepository,
                              final StudentMapper studentMapper,
                              final FacultyMapper facultyMapper
    ) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.studentMapper = studentMapper;
        this.facultyMapper = facultyMapper;
    }

    @Override
    public StudentDtoOut create(StudentDtoIn studentDtoIn) {
        return studentMapper.toDto(
                studentRepository.save(
                        studentMapper.toEntity(studentDtoIn)
                )
        );
    }

    @Override
    public StudentDtoOut get(Long id) {
        return studentRepository
                .findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    public StudentDtoOut update(Long id, StudentDtoIn studentDtoIn) {
        return studentRepository
                .findById(id)
                .map(oldStudent -> {
                    oldStudent.setName(studentDtoIn.getName());
                    oldStudent.setAge(studentDtoIn.getAge());
                    Optional.of(studentDtoIn.getFacultyId())
                            .ifPresent(facultyId ->
                                    oldStudent.setFaculty(
                                            facultyRepository.findById(facultyId)
                                                    .orElseThrow(() -> new FacultyNotFoundException(facultyId))
                                    )
                            );
                    return studentMapper.toDto(studentRepository.save(oldStudent));
                })
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    public StudentDtoOut delete(Long id) {
        Student student = studentRepository
                .findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
        return studentMapper.toDto(student);
    }

    @Override
    public List<StudentDtoOut> findAll(@Nullable Integer age) {
        return Optional.ofNullable(age)
                .map(studentRepository::findAllByAge)
                .orElseGet(studentRepository::findAll)
                .stream()
                .map(studentMapper::toDto)
                .toList();
    }

    @Override
    public List<StudentDtoOut> findByAgeBetween(Integer ageFrom, Integer ageTo) {
        return studentRepository
                .findAllByAgeBetween(ageFrom, ageTo)
                .stream()
                .map(studentMapper::toDto)
                .toList();
    }

    @Override
    public FacultyDtoOut getFacultyForStudent(Long id) {
        return studentRepository
                .findById(id)
                .map(Student::getFaculty)
                .map(facultyMapper::toDto)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

}
