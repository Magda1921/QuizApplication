package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Result;
import org.example.model.Student;
import org.example.repository.ResultRepository;
import org.example.repository.StudentRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class StudentServiceTest {
    private StudentService studentService;
    private StudentRepository studentRepository;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        studentRepository = mock(StudentRepository.class);
        System.setOut(new PrintStream(outContent));

        studentService = new StudentService(studentRepository);
    }

    @After
    public void after() {
        System.setOut(originalOut);
    }

    @Test
    public void shouldAddAndSaveANewStudent() {
//        given
        Student student = new Student();
        student.setId(1);
        student.setName("John");
//        when
        studentService.addAndSaveNewStudent(student);
//        then
        verify(studentRepository, times(1)).saveNewStudent(student);
    }

    @Test
    public void shouldFindStudentByStudentNameIfOneStudentWithSpecificNameExist() {
//        given
        Student student = new Student();
        int studentId = 2;
        String studentName = "Anna";
        student.setId(studentId);
        student.setName(studentName);
        List<Result> results = new ArrayList<>();
        student.setResults(results);

        List<Student> students = List.of(student);
        when(studentRepository.getListOfStudentsByStudentNameFromDB(studentName)).thenReturn(students);

//        when
        Student studentResult = studentService.findStudentByStudentName(studentName);
//        then
        verify(studentRepository, times(1)).getListOfStudentsByStudentNameFromDB(studentName);
        assertEquals(studentResult, student);
    }
    @Test
    public void shouldShowAllStudents() {
//       given
        Student student1 = new Student(1, "Ann");
        Student student2 = new Student(2, "John");

        List<Student> students = List.of(student1, student2);
        when(studentRepository.getListOfStudentsFromDB()).thenReturn(students);
//        when
        studentService.showAllStudents();
//        then
        String consoleOutput = outContent.toString();
        String expectedString = "Student name: Ann\r\n" +
                "Student name: John";
        assertEquals(expectedString, consoleOutput.trim());
        verify(studentRepository, times(1)).getListOfStudentsFromDB();
    }
}



