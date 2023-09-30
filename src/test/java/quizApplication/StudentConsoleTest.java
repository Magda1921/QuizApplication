package quizApplication;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.*;
import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Result;
import org.example.model.Student;
import org.junit.Before;
import org.junit.Test;

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
    private QuestionRepository questionRepository;
    TypedQuery<Question> query;
    TypedQuery<Student> query1;

    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        entityTransaction = mock(EntityTransaction.class);
        query = mock(TypedQuery.class);
        query1 = mock(TypedQuery.class);

        studentConsole = new StudentConsole(entityManager);
        studentRepository = new StudentRepository(entityManager);
        resultRepository = new ResultRepository(entityManager);
        questionRepository = new QuestionRepository(entityManager);
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
    public void shouldReturnListOfQuestionsReturnedFromDb() {
//        given
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
        when(entityManager.createQuery("select question from Question question")).thenReturn(query);
        when(query.getResultList()).thenReturn(questions);
//        when
        List<Question> results = studentConsole.showQuiz();
//        then
        assertEquals(questions, results);
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
        when(entityManager.createQuery("select student from Student student where student.name = ?1")).thenReturn(query1);
        when(query1.setParameter(eq(1), eq(studentName))).thenReturn(query1);
        when(query1.getResultList()).thenReturn(students);
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

}


