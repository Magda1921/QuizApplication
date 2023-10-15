package org.example;

import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Result;
import org.example.model.Student;
import org.example.repository.QuestionRepository;
import org.example.repository.ResultRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class QuizService {
    Scanner scanner = new Scanner(System.in);
    private StudentService studentService;
    private QuestionRepository questionRepository;
    private ResultRepository resultRepository;

    public QuizService(StudentService studentService, QuestionRepository questionRepository, ResultRepository resultRepository) {
        this.studentService = studentService;
        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;
    }

    public void play() {
        System.out.println("Enter the quiz topic you want to play");
        String quizTopic = scanner.nextLine();
        try {
            Student activeStudent = getExistingAccount();
            List<Question> quizQuestions = prepareQuestionsByQuizTopic(quizTopic);
            String corectAnswer = "";
            Integer numberOfCorrectAnswers = 0;
            Integer numberOfQuestions = 0;

            for (Question question : quizQuestions) {
                numberOfQuestions++;
                displayQuestion(question);
                prepareAnswers(question);
                System.out.println("Enter the right answer (a, b, c or d): ");
                String answerFromUser = scanner.nextLine();
                numberOfCorrectAnswers = countNumberOfCorrectAnswers(answerFromUser, corectAnswer, numberOfCorrectAnswers);
            }
            Result result = addNewResult(numberOfQuestions, numberOfCorrectAnswers, quizTopic, activeStudent);
            saveNewResult(result);
        } catch (RuntimeException e) {
            System.out.println("Please try play quiz again or contact support IT");
        }
    }

    List<Question> prepareQuestionsByQuizTopic(String quizTopic) {
        List<Question> questions = questionRepository.findQuestionsbyQuizTopic(quizTopic);
        List<Question> quizQuestions = questionRepository.randomQuestions(questions);
        return quizQuestions;
    }

    void prepareAnswers(Question question) {
        List<Answer> answers = question.getAnswers();
        List<String> abcd = new ArrayList<>();
        abcd.add("a");
        abcd.add("b");
        abcd.add("c");
        abcd.add("d");
        for (int i = 0; i < 4; i++) {
            HashMap<String, String> answerHashMap = new HashMap<String, String>();
            answerHashMap.put(answers.get(i).getAnswer(), abcd.get(i));
            displayAnswer(answerHashMap);

            if (answers.get(i).isCorrect()) {
                getCorrectAnswer(answerHashMap, answers, i);
            }
        }
    }

    String getCorrectAnswer(HashMap<String, String> answerHashMap, List<Answer> answers, int i) {
        String corectAnswer = answerHashMap.get(answers.get(i).getAnswer());
        return corectAnswer;
    }

    int countNumberOfCorrectAnswers(String answerFromUser, String correctAnswer, int numberOfCorrectAnswers) {
        if (answerFromUser.equals(correctAnswer)) {
            numberOfCorrectAnswers++;
        }
        return numberOfCorrectAnswers;
    }

    void displayQuestion(Question question) {
        System.out.println(question.getQuestion());
    }

    void displayAnswer(HashMap<String, String> answerHashMap) {
        System.out.println(answerHashMap);
    }

    Student getExistingAccount() {

        System.out.println("Do you have an account? Please enter true/false");
        Scanner scanner = new Scanner(System.in);
        boolean accountIsActive = scanner.nextBoolean();
        Student activeStudent = new Student();

        if (accountIsActive) {
            scanner.nextLine();
            System.out.println("Please enter your name");
            String studentName = scanner.nextLine();
            activeStudent = studentService.findStudentByStudentName(studentName);
        } else if (!accountIsActive) {
            System.out.println("Please add a new account");
            Student student = studentService.retrieveStudentFromUser();
            studentService.addAndSaveNewStudent(student);
        }
        return activeStudent;
    }

    Result addNewResult(int numberOfQuestions, int numberOfRightAnswers, String quizTopic, Student activeStudent) {
        double finalResult = (((double) numberOfRightAnswers / numberOfQuestions) * 100);
        Result result = new Result();
        result.setResult(finalResult);
        result.setStudent(activeStudent);
        result.setQuizTopic(quizTopic);
        return result;
    }

    void saveNewResult(Result result) {
        resultRepository.saveNewResult(result);
    }

    public void showListOfAvailableQuizzes() {
        List<Question> questions = questionRepository.getAllQuestions();
        questions.stream()
                .map(Question::getQuizTopic)
                .distinct()
                .forEach(System.out::println);
    }

    void findQuestionByPartOfDescription() {
        System.out.println("Write the part of the description of the question that you want to find");
        String partOfDescription = scanner.nextLine();
        List<Question> questions = questionRepository.findQuestionByPartOfName(partOfDescription);
        for (Question question : questions) {
            System.out.println(question.getQuestion());
        }
    }
}
