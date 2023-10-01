package quizApplication;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.AnswerRepository;
import org.example.QuestionRepository;
import org.example.model.Answer;
import org.example.model.Question;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class AnswerRepositoryTest {
    private AnswerRepository answerRepository;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        entityTransaction = mock(EntityTransaction.class);
        answerRepository = new AnswerRepository(entityManager);
    }

    @Test
    public void shouldSaveAnswer() {
//        given
        Question question = new Question();
        Answer answer = new Answer("answer1", true, question);

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//        when
        answerRepository.saveNewAnswer(answer);
//        then
        verify(entityManager, times(1)).persist(answer);
    }

    @Test
    public void shouldRemoveAnswer() {
        //        given
        Question question = new Question();
        Answer answer = new Answer("answer1", true, question);

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//        when
        answerRepository.removeAnswer(answer);
//        then
        verify(entityManager, times(1)).remove(eq(answer));
    }
}
