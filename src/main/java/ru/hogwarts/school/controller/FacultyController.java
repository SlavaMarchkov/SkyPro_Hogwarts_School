package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping(path = "/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    @Autowired
    public FacultyController(final FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable(value = "id") Long id) {
        Faculty foundFaculty = facultyService.findFaculty(id);
        if (foundFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Faculty> editFaculty(@PathVariable(value = "id") Long id,
                                               @RequestBody Faculty faculty
    ) {
        Faculty foundFaculty = facultyService.editFaculty(id, faculty);
        if (foundFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable(value = "id") Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> filterByColor(@RequestParam(required = false) String color) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyService.filterByColor(color));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

}
