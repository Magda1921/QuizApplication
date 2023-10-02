package quizApplication;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.StudentRepository;
import org.example.model.Student;
import org.junit.Before;
import org.junit.Test;

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
