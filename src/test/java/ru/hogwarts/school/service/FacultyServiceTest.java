package ru.hogwarts.school.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.impl.FacultyServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FacultyServiceTest {

    private FacultyService service;

    @BeforeEach
    void setUp() {
        this.service = new FacultyServiceImpl();
        service.addFaculty(new Faculty(1, "Faculty 1", "green"));
        service.addFaculty(new Faculty(2, "Faculty 2", "red"));
        service.addFaculty(new Faculty(3, "Faculty 3", "brown"));
        service.addFaculty(new Faculty(4, "Faculty 4", "yellow"));
    }

    @AfterEach
    void tearDown() {
        new ArrayList<>(service.getAll())
                .forEach((faculty -> service.deleteFaculty(faculty.getId())));
    }

    @Test
    void addFacultyTest() {
        int size = service.size();
        Faculty newFaculty = new Faculty(5, "New Faculty", "black");
        assertEquals(newFaculty, service.addFaculty(newFaculty));
        assertEquals(service.getAll().size(), size + 1);
    }

    @Test
    void findFacultyPositiveTest() {
        Faculty found = new Faculty(2, "Faculty 2", "red");
        assertEquals(service.findFaculty(2), found);
    }

    @Test
    void findFacultyNegativeTest() {
        int size = service.size();
        assertNull(service.findFaculty(size + 1));
    }

    @Test
    void editFacultyPositiveTest() {
        Faculty faculty = new Faculty(2, "Faculty 2", "black");
        int size = service.size();
        assertEquals(service.editFaculty(2, faculty), faculty);
        assertEquals(service.getAll().size(), size);
    }

    @Test
    void editFacultyNegativeTest() {
        Faculty faculty = new Faculty(5, "New Faculty", "black");
        int size = service.size();
        assertNull(service.editFaculty(5, faculty));
        assertEquals(service.getAll().size(), size);
    }

    @Test
    void deleteFacultyPositiveTest() {
        int size = service.size();
        Faculty faculty = new Faculty(1, "Faculty 1", "green");
        assertEquals(faculty, service.deleteFaculty(1));
        assertEquals(service.getAll().size(), size - 1);
    }

    @Test
    void deleteFacultyNegativeTest() {
        int size = service.size();
        assertNull(service.deleteFaculty(size + 1));
        assertEquals(service.getAll().size(), size);
    }

    @Test
    void filterByColorPositiveTest() {
        Faculty newGreenFaculty = new Faculty(5, "Green Faculty", "green");
        Faculty anotherGreenFaculty = new Faculty(6, "Another Green Faculty", "green");
        service.addFaculty(newGreenFaculty);
        service.addFaculty(anotherGreenFaculty);
        List<Faculty> faculties = List.of(service.findFaculty(1), newGreenFaculty, anotherGreenFaculty);
        assertIterableEquals(faculties, service.filterByColor("green"));
    }

    @Test
    void filterByColorNegativeTest() {
        List<Faculty> faculties = Collections.emptyList();
        assertIterableEquals(faculties, service.filterByColor("blue"));
    }

}