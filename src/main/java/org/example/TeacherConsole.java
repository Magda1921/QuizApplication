package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TeacherConsole {
    Scanner scanner = new Scanner(System.in);
    private EntityManager entityManager;

    public TeacherConsole(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addNewQuestion() {
        try{
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
                QuestionRepository questionRepository = new QuestionRepository(entityManager);
                boolean questionAlreadyExist = questionAlreadyExist(questionDescription);
                if (questionAlreadyExist) {
                    displayMessageIfAccountAlreadyExist();
                    break;
                } else {
                    List<Answer> answers = addAnswers(question);
                    questionRepository.saveNewQuestion(question);
                    saveAnswers(answers);
                }
                }
            }
        }
        catch (RuntimeException e) {
            System.out.println("Please try to add a new question again.");
        }
    }

    public void displayMessageIfAccountAlreadyExist() {
        System.out.println("I'm sorry, but we cannot add this question to our database because there is already a question with the exact same description.");
    }

    public List<Answer> addAnswers(Question question) {
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
            answers[i].setAnswer(answerDescription);
            answers[i].setCorrect(isCorrect);
            answers[i].setQuestion(question);
            if (falseAnswers == 3 & correctAnswers == 1) {
                return List.of(answers);
            } else {
                break;
            }
        }

        return List.of(answers);
    }

    public void saveAnswers(List<Answer> answers) {
        for (int j = 0; j < 4; j++) {
            AnswerRepository answerRepository = new AnswerRepository(entityManager);
            answerRepository.saveNewAnswer(answers.get(j));
        }
    }

    public boolean questionAlreadyExist(String questionDescription) {
        QuestionRepository questionRepository = new QuestionRepository(entityManager);
        try {
            Question question = questionRepository.findQuestionByDescription(questionDescription);
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public void removeQuestion() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the question id you want to delete");
        int id = scanner.nextInt();
        QuestionRepository questionRepository = new QuestionRepository(entityManager);
        Question question = findQuestionById(id);
        questionRepository.removeQuestion(question);

    }

    public List<Question> showAllQuestions() {
        List<Question> questions = entityManager.createQuery("select question from Question question").getResultList();
        return questions;
    }

    public Question findQuestionById(int id) {
        Question question = entityManager.find(Question.class, id);
        return question;
    }

    public void updateQuestionById(int id) {
        Question questionToUpdate = findQuestionById(id);

        List<Answer> answers = questionToUpdate.getAnswers();
        QuestionRepository questionRepository = new QuestionRepository(entityManager);
        questionRepository.removeQuestion(questionToUpdate);

        Question question = new Question();
        question.setId(id);
        System.out.println("Enter the new quiz topic: ");
        String newQuizTopic = scanner.nextLine();
        question.setQuizTopic(newQuizTopic);
        System.out.println("Enter the new question description");
        String newQuestionDescription = scanner.nextLine();
        question.setQuestion(newQuestionDescription);
        question.setAnswers(answers);

        questionRepository.saveNewQuestion(question);
    }

    public List<Result> showAllResults() {
        List<Result> results = entityManager.createQuery("select result from Result result").getResultList();
        return results;

    }

    public List<Result> findResultByStudentName(String studentName) {
        List<Result> results = entityManager.createQuery("select result from Result result join Student student on result.student.id = student.id where student.name = ?1", Result.class).setParameter(1, studentName)
                .getResultList();
        return results;

    }
}


