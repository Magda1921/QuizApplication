package quizApplication;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.AnswerRepository;
import org.example.QuestionRepository;
import org.example.model.Answer;
import org.example.model.Question;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class QuestionRepositoryTest {
    private QuestionRepository questionRepository;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;
    private TypedQuery<Question> query;

    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        entityTransaction = mock(EntityTransaction.class);
        query = mock(TypedQuery.class);
        questionRepository = new QuestionRepository(entityManager);
    }

    @Test
    public void shouldSaveQuestion() {
//        given
        List<Answer> answers = new ArrayList<>();
        String description = "description";
        String quizTopic = "topic";
        Question question = new Question(description, quizTopic, answers);

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//        when
        questionRepository.saveNewQuestion(question);
//        then
        verify(entityManager, times(1)).persist(question);
    }

    @Test
    public void shouldRemoveQuestion() {
        //        given
        List<Answer> answers = new ArrayList<>();
        String description = "description";
        String quizTopic = "topic";
        Question question = new Question(description, quizTopic, answers);

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//        when
        questionRepository.removeQuestion(question);
//        then
        verify(entityManager, times(1)).remove(eq(question));
    }

    @Test
    public void shouldFindQuestionByQuizTopic() {
//        given
        String topic = "topic";

        List<Answer> answers1 = new ArrayList<>();
        Question question1 = new Question();
        question1.setId(1);
        question1.setQuestion("questionDescription1");
        question1.setQuizTopic("topic");
        question1.setAnswers(answers1);

        List<Answer> answers2 = new ArrayList<>();
        Question question2 = new Question();
        question2.setId(2);
        question2.setQuestion("questionDescription2");
        question2.setQuizTopic("topic");
        question2.setAnswers(answers2);

        List<Question> questions = List.of(question1, question2);

        when(entityManager.createQuery("select question from Question question where question.quizTopic = ?1")).thenReturn(query);
        when(query.setParameter(1, topic)).thenReturn(query);
        when(query.getResultList()).thenReturn(questions);
//        when
        List<Question> results = questionRepository.findQuestionsbyQuizTopic(topic);
//        then
        assertEquals(questions, results);
    }

    @Test
    public void shouldFindQuestionByDescription() {
//        given
        List<Answer> answers1 = new ArrayList<>();
        Question question1 = new Question();
        question1.setId(1);
        String description = "questionDescription1";
        question1.setQuestion(description);
        question1.setQuizTopic("topic");
        question1.setAnswers(answers1);

        when(entityManager.createQuery("select question from Question question where question.question = ?1")).thenReturn(query);
        when(query.setParameter(1, description)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(question1);
//        when
        Question result = questionRepository.findQuestionByDescription(description);
//        then
        assertEquals(question1,result);
    }
}

