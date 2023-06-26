package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.impl.FacultyServiceImpl;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {

    @Mock
    private FacultyRepository repositoryMock;

    @InjectMocks
    private FacultyServiceImpl service;

    @Test
    void shouldAddFacultyTest() {
        Faculty newFaculty = new Faculty(1, "Faculty 1", "green");
        when(repositoryMock.save(newFaculty))
                .thenReturn(newFaculty);
        Faculty actual = service.addFaculty(newFaculty);
        Faculty expected = new Faculty(1, "Faculty 1", "green");
        assertThat(actual)
                .isEqualTo(expected);
        verify(repositoryMock, times(1)).save(expected);
    }

    @Test
    void findFacultyPositiveTest() {
        Faculty found = new Faculty(2, "Faculty 2", "green");
        when(repositoryMock.findById(2L)).thenReturn(Optional.of(found));
        assertEquals(service.findFaculty(2), found);
    }

    @Test
    void findFacultyNegativeTest() {
        when(repositoryMock.findById(3L)).thenReturn(Optional.empty());
        assertNull(service.findFaculty(3));
    }

    @Test
    void editFacultyPositiveTest() {
        Faculty Faculty = new Faculty(2, "Faculty 2", "green");
        when(repositoryMock.save(Faculty))
                .thenReturn(Faculty);
        Faculty actual = service.editFaculty(Faculty.getId(), Faculty);
        Faculty expected = new Faculty(2, "Faculty 2", "green");
        assertThat(actual)
                .isEqualTo(expected);
        verify(repositoryMock, times(1)).save(expected);
    }

    @Test
    void editFacultyNegativeTest() {
        Faculty faculty1 = new Faculty(2, "Faculty 2", "green");
        Faculty faculty2 = new Faculty(5, "Faculty 5", "red");
        assertThat(faculty1)
                .isNotEqualTo(faculty2);
        verify(repositoryMock, never()).save(faculty1);
    }

    @Test
    void deleteFacultyTest() {
        service.deleteFaculty(1);
        verify(repositoryMock, times(1)).deleteById(1L);
    }

    @Test
    void findByColorPositiveTest() {
        Faculty faculty1 = new Faculty(1, "Faculty 1", "green");
        Faculty faculty2 = new Faculty(2, "Faculty 2", "green");
        when(repositoryMock.findByColorLike("green")).thenReturn(
                List.of(faculty1, faculty2)
        );
        when(repositoryMock.findAll()).thenReturn(
                List.of(faculty1, faculty2)
        );
        assertThat(service.getAll()).isEqualTo(service.findByColor("green"));
        verify(repositoryMock, times(1)).findByColorLike("green");
    }

    @Test
    void findByColorNegativeTest() {
        when(repositoryMock.findByColorLike("green"))
                .thenReturn(emptyList());
        assertThat(service.findByColor("green")).isEmpty();
        verify(repositoryMock, times(1)).findByColorLike("green");
    }

}