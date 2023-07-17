package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final StudentMapper studentMapper;
    private final FacultyMapper facultyMapper;
    private final AvatarService avatarService;
    Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    public StudentServiceImpl(final StudentRepository studentRepository,
                              final FacultyRepository facultyRepository,
                              final StudentMapper studentMapper,
                              final FacultyMapper facultyMapper,
                              final AvatarService avatarService
    ) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.studentMapper = studentMapper;
        this.facultyMapper = facultyMapper;
        this.avatarService = avatarService;
    }

    @Override
    public StudentDtoOut create(StudentDtoIn studentDtoIn) {
        logger.info("Was invoked method for creating a Student");

        return studentMapper.toDto(
                studentRepository.save(
                        studentMapper.toEntity(studentDtoIn)
                )
        );
    }

    @Override
    public StudentDtoOut get(Long id) {
        logger.info("Was invoked method for getting the Student with id = {}", id);

        return studentRepository
                .findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    public StudentDtoOut update(Long id, StudentDtoIn studentDtoIn) {
        logger.info("Was invoked method for updating the Student with id = {}", id);

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

                    logger.warn("The Student with id = {} was successfully updated", id);

                    return studentMapper.toDto(studentRepository.save(oldStudent));
                })
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    public StudentDtoOut delete(Long id) {
        logger.info("Was invoked method for deleting the Student with id = {}", id);

        Student student = studentRepository
                .findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        studentRepository.delete(student);

        logger.warn("The Student with id = {} was successfully deleted", id);

        return studentMapper.toDto(student);
    }

    @Override
    public List<StudentDtoOut> findAll(@Nullable Integer age) {
        logger.info("Was invoked method for finding all the Students or the Students with age = {}", age);

        return Optional.ofNullable(age)
                .map(studentRepository::findAllByAge)
                .orElseGet(studentRepository::findAll)
                .stream()
                .map(studentMapper::toDto)
                .toList();
    }

    @Override
    public List<StudentDtoOut> findByAgeBetween(Integer ageFrom, Integer ageTo) {
        logger.info("Was invoked method for finding all the Students with age between {} and {}", ageFrom, ageTo);

        return studentRepository
                .findAllByAgeBetween(ageFrom, ageTo)
                .stream()
                .map(studentMapper::toDto)
                .toList();
    }

    @Override
    public FacultyDtoOut getFacultyForStudent(Long id) {
        logger.info("Was invoked method for getting the Faculty info for the Student with id = {}", id);

        return studentRepository
                .findById(id)
                .map(Student::getFaculty)
                .map(facultyMapper::toDto)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    public StudentDtoOut uploadAvatar(Long id, MultipartFile file) {
        logger.info("Was invoked method for uploading an Avatar for the Student with id = {}", id);

        Student student = studentRepository
                .findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        Avatar avatar = avatarService.create(student, file);
        student.setAvatar(avatar);

        logger.warn("Avatar for the Student with id = {} was successfully uploaded", id);

        return studentMapper.toDto(student);
    }

    @Override
    public int countAllStudentsInTheSchool() {
        logger.info("Was invoked method for counting all the Students in the School");

        return studentRepository.countAllStudentsInTheSchool();
    }

    @Override
    public double getAverageAgeOfStudents() {
        logger.info("Was invoked method for getting an average age of all the Students in the School");

        return studentRepository.getAverageAgeOfStudents();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDtoOut> getLastStudents(int count) {
        logger.info("Was invoked method for getting {} last added Students to the School", count);

        return studentRepository.getLastStudents(Pageable.ofSize(count))
                .stream()
                .map(studentMapper::toDto)
                .toList();
    }

}
