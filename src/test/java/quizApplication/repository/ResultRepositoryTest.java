package quizApplication.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Student;
import org.example.repository.ResultRepository;
import org.example.model.Result;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ResultRepositoryTest {
    private ResultRepository resultRepository;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;
    private TypedQuery<Result> query;

    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        entityTransaction = mock(EntityTransaction.class);
        resultRepository = new ResultRepository(entityManager);
        query = mock(TypedQuery.class);
    }

    @Test
    public void shouldSaveResultToDb() {
//       given
        Result result = new Result(1, "topic", 90);

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//        when
        resultRepository.saveNewResult(result);
//        then
        verify(entityManager, times(1)).persist(result);

    }

    @Test
    public void shouldGetAllResultFromDB() {
//        given
        Result result1 = new Result(1, "quizTopic", 50);
        Result result2 = new Result(2, "quizTopic", 90);

        List<Result> listOfResults = List.of(result1, result2);
        when(entityManager.createQuery("select result from Result result")).thenReturn(query);
        when(query.getResultList()).thenReturn(listOfResults);
//        when
        List<Result> results = resultRepository.getAllResultsFromDB();
//        then
        assertEquals(listOfResults, results);
    }

    @Test
    public void shouldGetListOfResultsBuStudentNameFromDB() {
        Result result1 = new Result(1, "quizTopic", 50);
        Result result2 = new Result(2, "quizTopic", 90);

        List<Result> listOfResults = List.of(result1, result2);
        String studentName = "Jan";
        Student student = new Student(1, studentName, listOfResults);

        when(entityManager.createQuery("select result from Result result join Student student on result.student.id = student.id where student.name = ?1", Result.class)).thenReturn(query);
        when(query.setParameter(eq(1), eq(studentName))).thenReturn(query);
        when(query.getResultList()).thenReturn(listOfResults);
//        when
        List<Result> results = resultRepository.getListOfResultsByStudentNameFromDB(studentName);
//        then
        assertEquals(listOfResults, results);
    }
}


