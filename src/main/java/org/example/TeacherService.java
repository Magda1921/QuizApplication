package org.example;

import jakarta.persistence.NoResultException;
import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Result;
import org.example.repository.AnswerRepository;
import org.example.repository.QuestionRepository;
import org.example.repository.ResultRepository;

import java.util.List;
import java.util.Scanner;

public class TeacherService {
    Scanner scanner = new Scanner(System.in);
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private ResultRepository resultRepository;

    public TeacherService(QuestionRepository questionRepository, AnswerRepository answerRepository, ResultRepository resultRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.resultRepository = resultRepository;
    }

    void addNewQuestion() {
        try {
            while (true) {
                Question question = new Question();
                System.out.println("Enter next question or click x for exit");
                String questionDescription = scanner.nextLine();
                if (questionDescription.equals("x")) {
                    break;
                } else {
                    System.out.println("What topic of the quiz should this question be assigned to?");
                    String quizTopic = scanner.nextLine();
                    question.setQuestion(questionDescription);
                    question.setQuizTopic(quizTopic);
                    Question questionAlreadyExist = findQuestionByDescription(questionDescription);
                    if (questionAlreadyExist != null) {
                        displayMessageIfAccountAlreadyExist();
                        break;
                    } else if (questionAlreadyExist == null) {
                        List<Answer> answers = addAnswers(question);
                        if (answers != null) {
                            saveQuestion(question);
                            saveAnswers(answers);
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            System.out.println("Please try to add a new question again.");
        }
    }

    void saveQuestion(Question question) {
        questionRepository.saveNewQuestion(question);
    }

    Question findQuestionByDescription(String questionDescription) {
        Question question = questionRepository.findQuestionByDescription(questionDescription);
        return question;
    }

    void displayMessageIfAccountAlreadyExist() {
        System.out.println("I'm sorry, but we cannot add this question to our database because there is already a question with the exact same description.");
    }

    List<Answer> addAnswers(Question question) {
        Answer[] answers = new Answer[4];
        int correctAnswers = 0;
        int falseAnswers = 0;
        for (int i = 0; i < 4; i++) {
            answers[i] = new Answer();
            System.out.println("Enter the " + (i + 1) + " answer");
            String answerDescription = scanner.nextLine();
            System.out.println("Is it a correct answer? If it's a correct answer, please enter true, else please enter false");
            boolean isCorrect;
            try {
                isCorrect = scanner.nextBoolean();
                scanner.nextLine();
                if (isCorrect) {
                    correctAnswers++;
                } else {
                    falseAnswers++;
                }
            } catch (RuntimeException e) {
                System.out.println("You need to enter true or false. Please try add a new question again.");
                break;
            }
            if (correctAnswers > 1) {
                System.out.println("You cannot add more than 1 correct answer, please try add a question again.");
                break;
            }
            if (falseAnswers > 3) {
                System.out.println("You cannot add more than 3 false answers, please try add a question again.");
                return null;
            }
            answers[i].setAnswer(answerDescription);
            answers[i].setCorrect(isCorrect);
            answers[i].setQuestion(question);
        }
        return List.of(answers);
    }

    void saveAnswers(List<Answer> answers) {
        for (int j = 0; j < 4; j++) {
            answerRepository.saveNewAnswer(answers.get(j));
        }
    }

    void removeQuestion(int id) {
        Question question = questionRepository.findQuestionById(id);
        questionRepository.removeQuestion(question);
    }

    void updateQuestionByProvidedId() {
        System.out.println("Write the id of the question that you want to update");
        int idToUpdate = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter the new quiz topic: ");
        String newQuizTopic = scanner.nextLine();
        System.out.println("Enter the new question description");
        String newQuestionDescription = scanner.nextLine();
        questionRepository.updateQuestionById(idToUpdate, newQuestionDescription, newQuizTopic);
    }

    public void showAllQuestions() {
        List<Question> questions = questionRepository.getAllQuestions();
        for (Question q : questions) {
            System.out.println("Id: " + q.getId());
            System.out.println("Question: " + q.getQuestion());
            System.out.println("Quiz topic: " + q.getQuizTopic());
        }
    }

    void showQuestionsByPartOfDescription() {
        System.out.println("Write the part of the description of the question that you want to find");
        String partOfDescription = scanner.nextLine();
        List<Question> questions = questionRepository.findQuestionByPartOfName(partOfDescription);
        for (Question question : questions) {
            System.out.println(question.getQuestion());
        }
    }

    void showAllResults() {
        List<Result> results = resultRepository.getAllResultsFromDB();
        for (Result result : results) {
            System.out.println("Result Id : " + result.getId());
            System.out.println("Result : " + result.getResult());
            System.out.println("Quiz topic : " + result.getQuizTopic());
            System.out.println("Student name : " + result.getStudent().getName());
        }
    }

    void showResultByStudentName() {
        System.out.println("Write the name of the student");
        String studentName = scanner.nextLine();
        List<Result> results = resultRepository.getListOfResultsByStudentNameFromDB(studentName);
        for (Result result : results) {
            System.out.println("Student name = " + result.getStudent().getName() + " Result = " + result.getResult() + " Quiz topic = " + result.getQuizTopic());
        }
    }
}
