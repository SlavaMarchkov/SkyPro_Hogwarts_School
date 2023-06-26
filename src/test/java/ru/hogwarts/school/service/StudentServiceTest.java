package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.impl.StudentServiceImpl;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository repositoryMock;

    @InjectMocks
    private StudentServiceImpl service;

    @Test
    void shouldAddStudentTest() {
        Student newStudent = new Student(1, "Student 1", 12);
        when(repositoryMock.save(newStudent))
                .thenReturn(newStudent);
        Student actual = service.addStudent(newStudent);
        Student expected = new Student(1, "Student 1", 12);
        assertThat(actual)
                .isEqualTo(expected);
        verify(repositoryMock, times(1)).save(expected);
    }

    @Test
    void findStudentPositiveTest() {
        Student found = new Student(2, "Student 2", 13);
        when(repositoryMock.findById(2L)).thenReturn(Optional.of(found));
        assertEquals(service.findStudent(2), found);
    }

    @Test
    void findStudentNegativeTest() {
        when(repositoryMock.findById(3L)).thenReturn(Optional.empty());
        assertNull(service.findStudent(3));
    }

    @Test
    void editStudentPositiveTest() {
        Student student = new Student(2, "Student 2", 13);
        when(repositoryMock.save(student))
                .thenReturn(student);
        Student actual = service.editStudent(student.getId(), student);
        Student expected = new Student(2, "Student 2", 13);
        assertThat(actual)
                .isEqualTo(expected);
        verify(repositoryMock, times(1)).save(expected);
    }

    @Test
    void editStudentNegativeTest() {
        Student student1 = new Student(2, "Student 2", 13);
        Student student2 = new Student(5, "Student 5", 14);
        assertThat(student1)
                .isNotEqualTo(student2);
        verify(repositoryMock, never()).save(student1);
    }

    @Test
    void deleteStudentTest() {
        service.deleteStudent(1);
        verify(repositoryMock, times(1)).deleteById(1L);
    }

    @Test
    void findByAgePositiveTest() {
        Student student1 = new Student(1, "Student 1", 12);
        Student student2 = new Student(2, "Student 2", 12);
        when(repositoryMock.findByAge(12)).thenReturn(
                List.of(student1, student2)
        );
        when(repositoryMock.findAll()).thenReturn(
                List.of(student1, student2)
        );
        assertThat(service.getAll()).isEqualTo(service.findByAge(12));
        verify(repositoryMock, times(1)).findByAge(12);
    }

    @Test
    void findByAgeNegativeTest() {
        when(repositoryMock.findByAge(12))
                .thenReturn(emptyList());
        assertThat(service.findByAge(12)).isEmpty();
        verify(repositoryMock, times(1)).findByAge(12);
    }

}