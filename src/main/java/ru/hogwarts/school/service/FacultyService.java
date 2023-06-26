package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;


public interface FacultyService {

    Faculty addFaculty(Faculty faculty);

    Faculty findFaculty(long id);

    Faculty editFaculty(long id, Faculty faculty);

    void deleteFaculty(long id);

    Collection<Faculty> findByColor(String color);

    Collection<Faculty> findByNameOrColor(String name, String color);

    Collection<Faculty> getAll();

    int size();

    Collection<Student> getFacultyStudents(Long id);

}
