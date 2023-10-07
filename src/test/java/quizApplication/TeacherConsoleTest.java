package quizApplication;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.example.repository.QuestionRepository;
import org.example.TeacherConsole;
import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Result;
import org.example.model.Student;
import org.example.repository.ResultRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import quizApplication.repository.QuestionRepositoryTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TeacherConsoleTest {

    private TeacherConsole teacherConsole;
    private EntityManager entityManagerMock;
    private QuestionRepository questionRepository;
    private ResultRepository resultRepository;
    private TypedQuery<Question> questionQuery;
    private TypedQuery<Result> resultQuery;
    private EntityTransaction entityTransaction;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setup() {
        entityManagerMock = mock(EntityManager.class);
        questionQuery = mock(TypedQuery.class);
        resultQuery = mock(TypedQuery.class);
        entityTransaction = mock(EntityTransaction.class);
        System.setOut(new PrintStream(outContent));

        questionRepository = new QuestionRepository(entityManagerMock);
        teacherConsole = new TeacherConsole(entityManagerMock);
        resultRepository = new ResultRepository(entityManagerMock);
    }

    @After
    public void after() {
        System.setOut(originalOut);
    }

    @Test
    public void questionShouldExist() {
//       given
        List<Answer> answers = new ArrayList<>();
        String description = "description";
        String quizTopic = "topic";
        Question question = new Question(description, quizTopic, answers);
//       when
        when(entityManagerMock.createQuery("select question from Question question where question.question = ?1")).thenReturn(questionQuery);
        when(questionQuery.setParameter(1, description)).thenReturn(questionQuery);
        when(questionQuery.getSingleResult()).thenReturn(question);
        when(questionRepository.findQuestionByDescription(description)).thenReturn(question);
//       then
        boolean result = teacherConsole.questionAlreadyExist(description);
        assertTrue(teacherConsole.questionAlreadyExist("description"));
    }

    @Test
    public void questionShouldNotExist() {
//       given
        List<Answer> answers = new ArrayList<>();
        String description = "description";
        String quizTopic = "topic";
        Question question = new Question(description, quizTopic, answers);
//       when
        when(entityManagerMock.createQuery("select question from Question question where question.question = ?1")).thenReturn(questionQuery);
        when(questionQuery.setParameter(1, description)).thenReturn(questionQuery);
        when(questionQuery.getSingleResult()).thenThrow(new NoResultException());
//       then
        boolean result = teacherConsole.questionAlreadyExist(description);
        assertFalse(result);
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
    public void shouldReturnListOfQuestionsReturnedFromDb() {
//      given
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
        when(entityManagerMock.createQuery("select question from Question question")).thenReturn(questionQuery);
        when(questionQuery.getResultList()).thenReturn(questions);

//        when
        List<Question> result = questionRepository.getAllQuestions();
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
        when(entityManagerMock.createQuery("select result from Result result")).thenReturn(resultQuery);
        when(resultQuery.getResultList()).thenReturn(resultsList);
//        when
        List<Result> results = resultRepository.getAllResultsFromDB();
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

        when(entityManagerMock.createQuery("select result from Result result join Student student on result.student.id = student.id where student.name = ?1", Result.class)).thenReturn(resultQuery);
        when(resultQuery.setParameter(eq(1), eq(studentName))).thenReturn(resultQuery);
        when(resultQuery.getResultList()).thenReturn(resultsList);
//        when
        List<Result> results = resultRepository.getListOfResultsByStudentNameFromDB(studentName);
//        then
        assertEquals(resultsList, results);
    }

    @Test
    public void shouldShowMessageToConsoleIfQuestionAlreadyExist() {
//        given
        boolean questionAlreadyExist = true;
//        when
        teacherConsole.displayMessageIfAccountAlreadyExist();
//        then
        String consoleOutput = outContent.toString();
        String expectedString = "I'm sorry, but we cannot add this question to our database because there is already a question with the exact same description.";
        assertEquals(expectedString, consoleOutput.trim());
    }
}



