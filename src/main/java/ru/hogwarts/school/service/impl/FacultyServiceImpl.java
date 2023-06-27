package ru.hogwarts.school.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyServiceImpl(final FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }


    @Override
    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty findFaculty(long id) {
        Optional<Faculty> foundFaculty = facultyRepository.findById(id);
        return foundFaculty.orElse(null);
    }

    @Override
    public Faculty editFaculty(long id, Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public void deleteFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    @Override
    public Collection<Faculty> findByColor(String color) {
        return facultyRepository.findByColorLike(color);
    }

    @Override
    public Collection<Faculty> findByNameOrColor(String name, String color) {
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    @Override
    public Collection<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    @Override
    public int size() {
        return getAll().size();
    }

    @Override
    public Collection<Student> getFacultyStudents(Long id) {
        Optional<Faculty> foundFaculty = Optional.ofNullable(findFaculty(id));
        if (foundFaculty.isPresent()) {
            return foundFaculty.get().getStudents();
        }
        return Collections.emptyList();
    }

}
