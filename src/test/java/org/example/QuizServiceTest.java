package org.example;

import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Result;
import org.example.model.Student;
import org.example.repository.QuestionRepository;
import org.example.repository.ResultRepository;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class QuizServiceTest {
    private QuizService quizService;
    private StudentService studentService;
    private QuestionRepository questionRepository;
    private ResultRepository resultRepository;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        questionRepository = mock(QuestionRepository.class);
        resultRepository = mock(ResultRepository.class);
        studentService = mock(StudentService.class);
        System.setOut(new PrintStream(outContent));

        quizService = new QuizService(studentService, questionRepository, resultRepository);
    }

    @Test
    public void shouldPrepareQuestionsByQuizTopic() {
//        given
        List<Answer> answers1 = new ArrayList<>();
        List<Answer> answers2 = new ArrayList<>();
        String quizTopic = "topic";
        Question question1 = new Question(1, "description1", quizTopic, answers1);
        Question question2 = new Question(2, "description2", quizTopic, answers2);
        List<Question> questions = List.of(question1, question2);
        when(questionRepository.findQuestionsbyQuizTopic(quizTopic)).thenReturn(questions);
//        when
        quizService.prepareQuestionsByQuizTopic(quizTopic);
//        then
        verify(questionRepository, times(1)).findQuestionsbyQuizTopic(quizTopic);
        verify(questionRepository, times(1)).randomQuestions(questions);
    }

    @Test
    public void shouldDisplayQuestion() {
//        given
        Question question1 = new Question();
        question1.setId(1);
        question1.setQuestion("questionDescription1");
        question1.setQuizTopic("topic");
        List<Answer> answers1 = new ArrayList<>();
        question1.setAnswers(answers1);
//        when
        quizService.displayQuestion(question1);
//        then
        String consoleOutput = outContent.toString();
        String expectedString = "questionDescription1";
        assertEquals(expectedString, consoleOutput.trim());
    }

    @Test
    public void shouldDisplayAnswer() {
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
        quizService.displayAnswer(answerHashMap);
//        then
        String consoleOutput = outContent.toString();
        String expectedString = "{answer1=a}";
        assertEquals(expectedString, consoleOutput.trim());
    }

    @Test
    public void shouldAddNewResult() {
//        given
        Student student = new Student(1, "Magda");
        Result result = new Result();
        String quizTopic = "topic";
        result.setStudent(student);
        result.setResult(75.0);
        result.setQuizTopic(quizTopic);
        result.setId(1);

//        when
        quizService.saveNewResult(result);
//        then
        verify(resultRepository, times(1)).saveNewResult(result);
    }

    @Test
    public void shouldShowAvailableQuizzes() {
//        given
        List<Answer> answers1 = new ArrayList<>();
        List<Answer> answers2 = new ArrayList<>();
        String quizTopic = "topic";
        Question question1 = new Question(1, "description1", quizTopic, answers1);
        Question question2 = new Question(2, "description2", quizTopic, answers2);
        List<Question> questions = List.of(question1, question2);
        when(questionRepository.getAllQuestions()).thenReturn(questions);
//        when
        quizService.showListOfAvailableQuizzes();
//        then
        verify(questionRepository, times(1)).getAllQuestions();
        String consoleOutput = outContent.toString();
        String expectedString = "topic";
        assertEquals(expectedString, consoleOutput.trim());
    }
}
