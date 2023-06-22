package ru.hogwarts.school.service;

class StudentServiceTest {

    private StudentService service;

    @BeforeEach
    void setUp() {
        this.service = new StudentServiceImpl();
        service.addStudent(new Student(1, "Student 1", 12));
        service.addStudent(new Student(2, "Student 2", 13));
        service.addStudent(new Student(3, "Student 3", 12));
        service.addStudent(new Student(4, "Student 4", 13));
    }

    @AfterEach
    void tearDown() {
        new ArrayList<>(service.getAll())
                .forEach((student -> service.deleteStudent(student.getId())));
    }

    @Test
    void addStudentTest() {
        int size = service.size();
        Student newStudent = new Student(5, "New Student", 12);
        assertEquals(newStudent, service.addStudent(newStudent));
        assertEquals(service.getAll().size(), size + 1);
    }

    @Test
    void findStudentPositiveTest() {
        Student found = new Student(2, "Student 2", 13);
        assertEquals(service.findStudent(2), found);
    }

    @Test
    void findStudentNegativeTest() {
        int size = service.size();
        assertNull(service.findStudent(size + 1));
    }

    @Test
    void editStudentPositiveTest() {
        Student Student = new Student(2, "Student 2", 13);
        int size = service.size();
        assertEquals(service.editStudent(2, Student), Student);
        assertEquals(service.getAll().size(), size);
    }

    @Test
    void editStudentNegativeTest() {
        Student Student = new Student(5, "New Student", 14);
        int size = service.size();
        assertNull(service.editStudent(5, Student));
        assertEquals(service.getAll().size(), size);
    }

    @Test
    void deleteStudentPositiveTest() {
        int size = service.size();
        Student Student = new Student(1, "Student 1", 12);
        assertEquals(Student, service.deleteStudent(1));
        assertEquals(service.getAll().size(), size - 1);
    }

    @Test
    void deleteStudentNegativeTest() {
        int size = service.size();
        assertNull(service.deleteStudent(size + 1));
        assertEquals(service.getAll().size(), size);
    }

    @Test
    void filterByAgePositiveTest() {
        Student newStudent = new Student(5, "New Student", 12);
        Student anotherStudent = new Student(6, "Another New Student", 12);
        service.addStudent(newStudent);
        service.addStudent(anotherStudent);
        List<Student> students = List.of(service.findStudent(1), service.findStudent(3), newStudent, anotherStudent);
        assertIterableEquals(students, service.filterByAge(12));
    }

    @Test
    void filterByAgeNegativeTest() {
        List<Student> faculties = Collections.emptyList();
        assertIterableEquals(faculties, service.filterByAge(15));
    }

}