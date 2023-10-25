package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.example.repository.AnswerRepository;
import org.example.repository.QuestionRepository;
import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Result;
import org.example.model.Student;
import org.example.repository.ResultRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TeacherServiceTest {
    private TeacherService teacherService;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private ResultRepository resultRepository;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setup() {
        questionRepository = mock(QuestionRepository.class);
        answerRepository = mock(AnswerRepository.class);
        resultRepository = mock(ResultRepository.class);

        System.setOut(new PrintStream(outContent));
        teacherService = new TeacherService(questionRepository, answerRepository, resultRepository);
    }

    @After
    public void after() {
        System.setOut(originalOut);
    }

    @Test
    public void shouldShowMessageToConsoleIfQuestionAlreadyExist() {
//        given
//        when
        teacherService.displayMessageIfAccountAlreadyExist();
//        then
        String consoleOutput = outContent.toString();
        String expectedString = "I'm sorry, but we cannot add this question to our database because there is already a question with the exact same description.";
        assertEquals(expectedString, consoleOutput.trim());
    }

    @Test
    public void shouldFindQuestionByDescription() {
//        given
        List<Answer> answers = new ArrayList<>();
        String description = "description";
        String quizTopic = "topic";
        Question question = new Question(description, quizTopic, answers);
        when(questionRepository.findQuestionByDescription(description)).thenReturn(question);
//        when
        Question result = teacherService.findQuestionByDescription(description);
//        then
        verify(questionRepository, times(1)).findQuestionByDescription(description);
        assertEquals(question, result);
    }

    @Test
    public void shouldSaveNewQuestion() {
//        given
        List<Answer> answers = new ArrayList<>();
        String description = "description";
        String quizTopic = "topic";
        Question question = new Question(description, quizTopic, answers);
//        when
        teacherService.saveQuestion(question);
//        then
        verify(questionRepository, times(1)).saveNewQuestion(question);
    }

    @Test
    public void shouldSave4Answers() {
//        given
        Answer answer1 = new Answer("answer1", true);
        Answer answer2 = new Answer("answer2", false);
        Answer answer3 = new Answer("answer3", false);
        Answer answer4 = new Answer("answer4", false);
        List<Answer> answers = List.of(answer1, answer2, answer3, answer4);
//        when
        teacherService.saveAnswers(answers);
//        then
        verify(answerRepository, times(1)).saveNewAnswer(answer1);
        verify(answerRepository, times(1)).saveNewAnswer(answer2);
        verify(answerRepository, times(1)).saveNewAnswer(answer3);
        verify(answerRepository, times(1)).saveNewAnswer(answer4);
    }

    @Test
    public void shouldRemoveQuestion() {
//        given
        Answer answer1 = new Answer("answer1", true);
        Answer answer2 = new Answer("answer2", false);
        Answer answer3 = new Answer("answer3", false);
        Answer answer4 = new Answer("answer4", false);
        List<Answer> answers = List.of(answer1, answer2, answer3, answer4);
        String description = "description";
        String quizTopic = "topic";
        int id = 1;
        Question question = new Question(id, description, quizTopic, answers);
        when(questionRepository.findQuestionById(id)).thenReturn(question);
//        when
        teacherService.removeQuestion(id);

//        then
        verify(questionRepository, times(1)).findQuestionById(id);
        verify(questionRepository, times(1)).removeQuestion(question);
    }

    @Test
    public void shouldShowAllQuestions() {
//        given
        List<Answer> answers1 = new ArrayList<>();
        List<Answer> answers2 = new ArrayList<>();
        Question question1 = new Question(1, "description1", "topic", answers1);
        Question question2 = new Question(2, "description2", "topic", answers2);
        List<Question> questions = List.of(question1, question2);
        when(questionRepository.getAllQuestions()).thenReturn(questions);
//        when
        teacherService.showAllQuestions();
//        then
        String consoleOutput = outContent.toString();
        String expectedString = "Id: 1\r\n" +
                "Question: description1\r\n" +
                "Quiz topic: topic\r\n" +
                "Id: 2\r\n" +
                "Question: description2\r\n" +
                "Quiz topic: topic";
        assertEquals(expectedString, consoleOutput.trim());
        verify(questionRepository, times(1)).getAllQuestions();
    }

    @Test
    public void shouldShowAllResults() {
//        given
        Student student1 = new Student(1, "Ann");
        Student student2 = new Student(2, "John");
        Result result1 = new Result(1, "topic", 90, student1);
        Result result2 = new Result(2, "topic", 75, student2);
        List<Result> results = List.of(result1, result2);
        when(resultRepository.getAllResultsFromDB()).thenReturn(results);
//        when
        teacherService.showAllResults();
//        then
        String consoleOutput = outContent.toString();
        String expectedString = "Result Id : 1\r\n" +
                "Result : 90.0\r\n" +
                "Quiz topic : topic\r\n" +
                "Student name : Ann\r\n" +
                "Result Id : 2\r\n" +
                "Result : 75.0\r\n" +
                "Quiz topic : topic\r\n" +
                "Student name : John";
        assertEquals(expectedString, consoleOutput.trim());
        verify(resultRepository, times(1)).getAllResultsFromDB();
    }
}



