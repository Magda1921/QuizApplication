package quizApplication;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.QuestionRepository;
import org.example.StudentRepository;
import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Student;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class StudentRepositoryTest {
    private StudentRepository studentRepository;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        entityTransaction = mock(EntityTransaction.class);
        studentRepository = new StudentRepository(entityManager);
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
}
