package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SchoolExceptionHandler {

    @ExceptionHandler({FacultyNotFoundException.class, StudentNotFoundException.class})
    public ResponseEntity<String> handleNotFound(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

}
