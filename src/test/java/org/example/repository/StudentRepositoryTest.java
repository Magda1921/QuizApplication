package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.model.Result;
import org.example.repository.StudentRepository;
import org.example.model.Student;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class StudentRepositoryTest {
    private StudentRepository studentRepository;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;
    private TypedQuery<Student> query;

    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        entityTransaction = mock(EntityTransaction.class);
        studentRepository = new StudentRepository(entityManager);
        query = mock(TypedQuery.class);
    }
    @Test
    public void shouldSaveStudentToDb() {
//       given
        Student student = new Student(1, "student");

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//        when
        studentRepository.saveNewStudent(student);
//        then
        verify(entityManager, times(1)).persist(student);
    }
    @Test
    public void shouldReturnListOfStudentsReturnedFromDb() {
//      given
        Student student1 = new Student(1, "Jan");
        Student student2 = new Student(2, "Maria");

        List<Student> students = List.of(student1, student2);
        when(entityManager.createQuery("select student from Student student")).thenReturn(query);
        when(query.getResultList()).thenReturn(students);

//        when
        List<Student> result = studentRepository.getListOfStudentsFromDB();
//        then
        assertEquals(students, result);
    }
    @Test
    public void shouldGetListOfStudentsByStudentNameFromDB() {
//        given
        Student student = new Student();
        int studentId = 2;
        String studentName = "Anna";
        student.setId(studentId);
        student.setName(studentName);
        List<Result> results = new ArrayList<>();
        student.setResults(results);

        List<Student> students = List.of(student);
        when(entityManager.createQuery("select student from Student student where student.name = ?1")).thenReturn(query);
        when(query.setParameter(eq(1), eq(studentName))).thenReturn(query);
        when(query.getResultList()).thenReturn(students);
//        when
        List<Student> result = studentRepository.getListOfStudentsByStudentNameFromDB(studentName);
//        then
        assertEquals(students, result);
    }
    @Test
    public void shouldGetStudentByStudentIdFromDB() {
//        given
        Student student = new Student();
        int studentId = 2;
        String studentName = "Anna";
        student.setId(studentId);
        student.setName(studentName);
        List<Result> results = new ArrayList<>();
        student.setResults(results);

        when(entityManager.createQuery("select student from Student student where student.id = ?1")).thenReturn(query);
        when(query.setParameter(eq(1), eq(studentId))).thenReturn(query);
        when(query.getSingleResult()).thenReturn(student);
//        when
        Student studentResult = studentRepository.getStudentByStudentIdFromDB(studentId);
//        then
        assertEquals(student, studentResult);
    }
}
