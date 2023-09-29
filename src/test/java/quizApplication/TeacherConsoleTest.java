package quizApplication;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.QuestionRepository;
import org.example.TeacherConsole;
import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Result;
import org.example.model.Student;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TeacherConsoleTest {

    private TeacherConsole teacherConsole;
    private EntityManager entityManagerMock;
    private QuestionRepository questionRepository;
    private TypedQuery<Question> query;
    private TypedQuery<Result> query1;
    private EntityTransaction entityTransaction;

    @Before
    public void setup() {
        entityManagerMock = mock(EntityManager.class);
        query = mock(TypedQuery.class);
        query1 = mock(TypedQuery.class);
        entityTransaction = mock(EntityTransaction.class);

        questionRepository = new QuestionRepository(entityManagerMock);
        teacherConsole = new TeacherConsole(entityManagerMock);
    }

    @Test
    public void questionShouldExist() {
//       given
        List<Answer> answers = new ArrayList<>();
        String description = "description";
        String quizTopic = "topic";
        Question question = new Question(description, quizTopic, answers);
//       when
        when(entityManagerMock.createQuery("select question from Question question where question.question = ?1")).thenReturn(query);
        when(query.setParameter(1, description)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(question);
        when(questionRepository.findQuestionByDescription(description)).thenReturn(question);
//       then
        boolean result = teacherConsole.questionAlreadyExist(description);
        assertTrue(teacherConsole.questionAlreadyExist("description"));
    }

    @Test
    public void shouldRemoveQuestion() {
//        given
        int quesionId = 1;
        Question question = new Question();
        List<Answer> answers = new ArrayList<>();
        String description = "description";
        String quizTopic = "topic";
        question.setId(quesionId);
        question.setQuestion(description);
        question.setQuizTopic(quizTopic);
        question.setAnswers(answers);

        when(entityManagerMock.getTransaction()).thenReturn(entityTransaction);

//        when
        teacherConsole.findQuestionById(quesionId);
        questionRepository.removeQuestion(question);
//       then
        verify(entityManagerMock, times(1)).find(Question.class, quesionId);
        verify(entityManagerMock, times(1)).remove(eq(question));
    }

    @Test
    public void shouldAddNewQuestion() {
//        given
        Question question = new Question();
        String questionDescription = "description";
        question.setId(1);
        question.setQuestion(questionDescription);
        question.setQuizTopic("topic");

        List<Answer> answers = new ArrayList<>();
        Answer answer1 = new Answer("answer1", true, question);
        Answer answer2 = new Answer("answer2", false, question);
        Answer answer3 = new Answer("answer3", false, question);
        Answer answer4 = new Answer("answer4", false, question);

        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        answers.add(answer4);

        question.setAnswers(answers);

        when(entityManagerMock.getTransaction()).thenReturn(entityTransaction);

//        when
        questionRepository.saveNewQuestion(question);
        teacherConsole.saveAnswers(answers);
//        then
        verify(entityManagerMock, times(1)).persist(question);
        verify(entityManagerMock, times(1)).persist(answer1);
        verify(entityManagerMock, times(1)).persist(answer2);
        verify(entityManagerMock, times(1)).persist(answer3);
        verify(entityManagerMock, times(1)).persist(answer4);

    }


    @Test
    public void shouldReturnListOfQuestionsReturnedFromDb() {
//given
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
        when(entityManagerMock.createQuery("select question from Question question")).thenReturn(query);
        when(query.getResultList()).thenReturn(questions);

//        when
        List<Question> result = teacherConsole.showAllQuestions();
//        then
        assertEquals(questions, result);
    }

    @Test
    public void shouldInvokeFindMethodOfEntityManagerWithQuestionIdPassedToRepository() {
//        given
        int questionId = 1;

//        when
        teacherConsole.findQuestionById(questionId);

//        then
        verify(entityManagerMock, times(1)).find(Question.class, questionId);
    }

    @Test
    public void shouldUpdateQuestionById() {
//        given
        int questionId = 1;

        Question question1 = new Question();
        List<Answer> answers1 = new ArrayList<>();
        String questionDescription = "description";
        String quizTopic = "topic";
        question1.setQuestion(questionDescription);
        question1.setQuizTopic(quizTopic);
        question1.setAnswers(answers1);
        question1.setId(questionId);

        Question question2 = new Question();
        List<Answer> answers = new ArrayList<>();
        question2.setId(1);
        question2.setQuestion("description");
        question2.setQuizTopic("topic");
        question2.setAnswers(answers1);
        question2.setAnswers(answers);

        when(entityManagerMock.getTransaction()).thenReturn(entityTransaction);
//        when
        teacherConsole.findQuestionById(questionId);
        questionRepository.removeQuestion(question1);
        questionRepository.saveNewQuestion(question2);
//        then
        verify(entityManagerMock, times(1)).find(Question.class, questionId);
        verify(entityManagerMock, times(1)).remove(eq(question1));
        verify(entityManagerMock, times(1)).persist(question2);

    }

    @Test
    public void shouldReturnListOfResultsReturnedFromDb() {
//        given
        Student student = new Student(1, "Magda");

        Result result1 = new Result();
        result1.setId(1);
        result1.setResult(90);
        result1.setQuizTopic("topic");
        result1.setStudent(student);

        Result result2 = new Result();
        result2.setId(2);
        result2.setResult(78);
        result2.setQuizTopic("topic2");
        result2.setStudent(student);

        List<Result> resultsList = List.of(result1, result2);
        when(entityManagerMock.createQuery("select result from Result result")).thenReturn(query1);
        when(query1.getResultList()).thenReturn(resultsList);
//        when
        List<Result> results = teacherConsole.showAllResults();
//        then
        assertEquals(resultsList, results);
    }

    @Test
    public void shouldReturnListOfResultsForSpecificStudentReturnedFromDb() {
//        given
        Student student = new Student();
        String studentName = "Magda";
        student.setId(1);
        student.setName(studentName);

        Result result1 = new Result();
        result1.setId(1);
        result1.setResult(90);
        result1.setQuizTopic("topic");
        result1.setStudent(student);

        Result result2 = new Result();
        result2.setId(2);
        result2.setResult(78);
        result2.setQuizTopic("topic2");
        result2.setStudent(student);

        List<Result> resultsList = List.of(result1, result2);

        student.setResults(resultsList);

        when(entityManagerMock.createQuery("select result from Result result join Student student on result.student.id = student.id where student.name = ?1", Result.class)).thenReturn(query1);
        when(query1.setParameter(eq(1), eq(studentName))).thenReturn(query1);
        when(query1.getResultList()).thenReturn(resultsList);
//        when
        List<Result> results = teacherConsole.findResultByStudentName(studentName);
//        then
        assertEquals(resultsList, results);
    }
}
