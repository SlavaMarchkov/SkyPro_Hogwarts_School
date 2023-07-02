package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping(path = "/student")
@Tag(name = "Контроллер по работе со студентами")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(final StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping(path = "{id}")
    public StudentDtoOut get(@PathVariable(value = "id") Long id) {
        return studentService.get(id);
    }

    @PostMapping
    public StudentDtoOut create(@RequestBody StudentDtoIn studentDtoIn) {
        return studentService.create(studentDtoIn);
    }

    @PutMapping(path = "{id}")
    public StudentDtoOut update(@PathVariable(value = "id") Long id,
                                @RequestBody StudentDtoIn studentDtoIn
    ) {
        return studentService.update(id, studentDtoIn);
    }

    @DeleteMapping(path = "{id}")
    public StudentDtoOut delete(@PathVariable(value = "id") Long id) {
        return studentService.delete(id);
    }

    @GetMapping
    public List<StudentDtoOut> findAll(@RequestParam(required = false) Integer age) {
        return studentService.findAll(age);
    }

    @GetMapping(path = "filter")
    public List<StudentDtoOut> findByAgeBetween(@RequestParam Integer ageFrom,
                                                @RequestParam Integer ageTo) {
        return studentService.findByAgeBetween(ageFrom, ageTo);
    }

    @GetMapping(path = "{id}/faculty")
    public FacultyDtoOut getFacultyForStudent(@PathVariable(value = "id") Long id) {
        return studentService.getFacultyForStudent(id);
    }

}
