package ru.hogwarts.school.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.dto.FacultyDtoIn;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final FacultyMapper facultyMapper;
    private final StudentMapper studentMapper;

    @Autowired
    public FacultyServiceImpl(final FacultyRepository facultyRepository,
                              final StudentRepository studentRepository,
                              final FacultyMapper facultyMapper,
                              final StudentMapper studentMapper
    ) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
        this.facultyMapper = facultyMapper;
        this.studentMapper = studentMapper;
    }

    @Override
    public FacultyDtoOut create(FacultyDtoIn facultyDtoIn) {
        return facultyMapper.toDto(
                facultyRepository.save(
                        facultyMapper.toEntity(facultyDtoIn)
                )
        );
    }

    @Override
    public FacultyDtoOut get(Long id) {
        return facultyRepository
                .findById(id)
                .map(facultyMapper::toDto)
                .orElseThrow(() -> new FacultyNotFoundException(id));
    }

    @Override
    public FacultyDtoOut update(Long id, FacultyDtoIn facultyDtoIn) {
        return facultyRepository
                .findById(id)
                .map(oldFaculty -> {
                    oldFaculty.setName(facultyDtoIn.getName());
                    oldFaculty.setColor(facultyDtoIn.getColor());
                    return facultyMapper.toDto(facultyRepository.save(oldFaculty));
                })
                .orElseThrow(() -> new FacultyNotFoundException(id));
    }

    @Override
    public FacultyDtoOut delete(Long id) {
        Faculty faculty = facultyRepository
                .findById(id)
                .orElseThrow(() -> new FacultyNotFoundException(id));
        facultyRepository.delete(faculty);
        return facultyMapper.toDto(faculty);
    }

    @Override
    public List<FacultyDtoOut> findByColorOrName(String colorOrName) {
        return facultyRepository
                .findAllByColorContainingIgnoreCaseOrNameContainingIgnoreCase(colorOrName, colorOrName)
                .stream()
                .map(facultyMapper::toDto)
                .toList();
    }

    @Override
    public List<FacultyDtoOut> findAll(@Nullable String color) {
        return Optional.ofNullable(color)
                .map(facultyRepository::findAllByColor)
                .orElseGet(facultyRepository::findAll)
                .stream()
                .map(facultyMapper::toDto)
                .toList();
    }

    @Override
    public List<StudentDtoOut> getFacultyStudents(Long id) {
        return studentRepository
                .findAllByFaculty_Id(id)
                .stream()
                .map(studentMapper::toDto)
                .toList();
    }

}
