package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.entity.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllByAge(Integer age);

    List<Student> findAllByAgeBetween(Integer ageFrom, Integer ageTo);

    List<Student> findAllByFaculty_Id(Long facultyId);

}
