package ru.hogwarts.school.dto;

public class StudentDtoIn {

    private String name;
    private int age;
    private long facultyId;

    public StudentDtoIn() {
    }

    public StudentDtoIn(String name, int age, long facultyId) {
        this.name = name;
        this.age = age;
        this.facultyId = facultyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(long facultyId) {
        this.facultyId = facultyId;
    }
}
