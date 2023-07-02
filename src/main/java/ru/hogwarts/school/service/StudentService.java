package ru.hogwarts.school.service;

import org.springframework.lang.Nullable;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;

import java.util.List;

public interface StudentService {

    StudentDtoOut create(StudentDtoIn studentDtoIn);

    StudentDtoOut get(Long id);

    StudentDtoOut update(Long id, StudentDtoIn studentDtoIn);

    StudentDtoOut delete(Long id);

    List<StudentDtoOut> findAll(@Nullable Integer age);

    List<StudentDtoOut> findByAgeBetween(Integer ageFrom, Integer ageTo);

    FacultyDtoOut getFacultyForStudent(Long id);

}
