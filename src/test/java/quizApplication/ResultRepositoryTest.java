package quizApplication;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.ResultRepository;
import org.example.StudentRepository;
import org.example.model.Result;
import org.example.model.Student;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ResultRepositoryTest {
    private ResultRepository resultRepository;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        entityTransaction = mock(EntityTransaction.class);
        resultRepository = new ResultRepository(entityManager);
    }
    @Test
    public void shouldSaveStudentToDb() {
//       given
        Result result = new Result(1, "topic", 90);

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//        when
        resultRepository.saveNewResult(result);
//        then
        verify(entityManager, times(1)).persist(result);

    }
}

