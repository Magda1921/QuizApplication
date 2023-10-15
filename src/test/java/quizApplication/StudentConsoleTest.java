package quizApplication;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.*;
import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Result;
import org.example.model.Student;
import org.example.repository.QuestionRepository;
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

public class StudentConsoleTest {
    private StudentConsole studentConsole;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;
    private StudentRepository studentRepository;
    private ResultRepository resultRepository;
    TypedQuery<Question> questionQuery;
    TypedQuery<Student> studentQuery;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        entityTransaction = mock(EntityTransaction.class);
        questionQuery = mock(TypedQuery.class);
        studentQuery = mock(TypedQuery.class);
        System.setOut(new PrintStream(outContent));

        studentConsole = new StudentConsole(entityManager);
        studentRepository = new StudentRepository(entityManager);
        resultRepository = new ResultRepository(entityManager);
    }

    @After
    public void after() {
        System.setOut(originalOut);
    }

    @Test
    public void shouldAddNewStudent() {
//        given
        Student student = new Student(1, "Magda");
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//        when
        studentRepository.saveNewStudent(student);
//        then
        verify(entityManager, times(1)).persist(student);

    }

    @Test
    public void shouldAddNewResult() {
//        given
        Student student = new Student(1, "Magda");

        Result result = new Result();
        result.setStudent(student);
        result.setResult(90);
        result.setQuizTopic("topic");
        result.setId(1);

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//        when
        resultRepository.saveNewResult(result);
//        then
        verify(entityManager, times(1)).persist(result);
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
        when(entityManager.createQuery("select student from Student student where student.name = ?1")).thenReturn(studentQuery);
        when(studentQuery.setParameter(eq(1), eq(studentName))).thenReturn(studentQuery);
        when(studentQuery.getResultList()).thenReturn(students);
//        when
        Student studentResult = studentConsole.findStudentByStudentName(studentName);
//        then
        assertEquals(student, studentResult);
    }

    @Test
    public void shouldGetTheCorrectAnswer() {
//        given
        Question question1 = new Question();
        Answer answer1 = new Answer("answer1", true, question1);
        Answer answer2 = new Answer("answer2", false, question1);
        Answer answer3 = new Answer("answer3", false, question1);
        Answer answer4 = new Answer("answer4", false, question1);
        List<Answer> answers1 = List.of(answer1, answer2, answer3, answer4);
        question1.setAnswers(answers1);
        List<String> abcd = new ArrayList<>();
        abcd.add("a");
        abcd.add("b");
        abcd.add("c");
        abcd.add("d");

        String corectAnswer = "";
//        when
        for (int i = 0; i < 4; i++) {
            HashMap<String, String> answerHashMap = new HashMap<String, String>();
            answerHashMap.put(answers1.get(i).getAnswer(), abcd.get(i));
            if (answers1.get(i).isCorrect()) {
                corectAnswer = answerHashMap.get(answers1.get(i).getAnswer());
            }
        }
//        then
        assertEquals("a", corectAnswer);
    }

    @Test
    public void shouldCountOfCorrectAnswersIfUserAnsweredCorrect() {
//        given
        String answerFromUser = "a";
        String correctAnswer = "a";
        int numberOfCorrectAnswers = 0;
//        when
        if (answerFromUser.equals(correctAnswer)) {
            numberOfCorrectAnswers++;
        }
//        then
        assertEquals(1, numberOfCorrectAnswers);
    }

    @Test
    public void shouldCountOfCorrectAnswersIfUserAnsweredIncorrect() {
//        given
        String answerFromUser = "b";
        String correctAnswer = "a";
        int numberOfCorrectAnswers = 0;
//        when
        if (answerFromUser.equals(correctAnswer)) {
            numberOfCorrectAnswers++;
        }
//        then
        assertEquals(0, numberOfCorrectAnswers);
    }

    @Test
    public void shouldDisplayQuestionToConsole() {
//        given
        Question question1 = new Question();
        question1.setId(1);
        question1.setQuestion("questionDescription1");
        question1.setQuizTopic("topic");
        List<Answer> answers1 = new ArrayList<>();
        question1.setAnswers(answers1);

//        when
        studentConsole.displayQuestion(question1);
//        then
        String consoleOutput = outContent.toString();
        String expectedString = "questionDescription1";
        assertEquals(expectedString, consoleOutput.trim());
    }

    @Test
    public void shouldDisplayAnswerToConsole() {
//        given
        Question question1 = new Question();
        Answer answer1 = new Answer("answer1", true, question1);
        Answer answer2 = new Answer("answer2", false, question1);
        Answer answer3 = new Answer("answer3", false, question1);
        Answer answer4 = new Answer("answer4", false, question1);
        List<Answer> answers = List.of(answer1, answer2, answer3, answer4);
        question1.setAnswers(answers);
        List<String> abcd = new ArrayList<>();
        abcd.add("a");
        abcd.add("b");
        abcd.add("c");
        abcd.add("d");

        HashMap<String, String> answerHashMap = new HashMap<String, String>();
        answerHashMap.put(answer1.getAnswer(), abcd.get(0));

//        when
        studentConsole.displayAnswer(answerHashMap);
//        then
        String consoleOutput = outContent.toString();
        String expectedString = "{answer1=a}";
        assertEquals(expectedString, consoleOutput.trim());
    }

    @Test
    public void shouldGetTheExistingStudentWhenAccountIsActive() {
//        given
        boolean accountIsActive = true;
        String studentName = "user";
        Student activeStudent = new Student(1,studentName);
        List <Student> students = List.of(activeStudent);

        when(entityManager.createQuery("select student from Student student where student.name = ?1")).thenReturn(studentQuery);
        when(studentQuery.setParameter(eq(1), eq(studentName))).thenReturn(studentQuery);
        when(studentQuery.getResultList()).thenReturn(students);
//        when
        Student studentResult = studentConsole.findStudentByStudentName(studentName);
//        then
        assertEquals(activeStudent, studentResult);
    }

    @Test
    public void shouldGetTheExistingStudentWhenAccountIsInactive() {
//        given
        boolean accountIsActive = false;
        Student student = new Student(1, "Magda");
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//        when
        studentRepository.saveNewStudent(student);
//        then
        verify(entityManager, times(1)).persist(student);

    }
}



