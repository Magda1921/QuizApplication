package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.model.Answer;
import org.example.model.Question;
import org.example.repository.AnswerRepository;
import org.example.repository.QuestionRepository;

import java.util.List;
import java.util.Scanner;

public class TeacherConsole {
    Scanner scanner = new Scanner(System.in);
    private EntityManager entityManager;

    public TeacherConsole(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addNewQuestion() {

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
                    QuestionRepository questionRepository = new QuestionRepository(entityManager);
                    boolean questionAlreadyExist = questionAlreadyExist(questionDescription);
                    if (questionAlreadyExist) {
                        displayMessageIfAccountAlreadyExist();
                        break;
                    } else {
                        List<Answer> answers = addAnswers(question);
                        if (answers != null) {
                            questionRepository.saveNewQuestion(question);
                            saveAnswers(answers);
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
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

    public Question findQuestionById(int id) {
        Question question = entityManager.find(Question.class, id);
        return question;
    }

    public void updateQuestionByProvidedId(int id, String newQuestionDescription, String newQuizTopic) {
        QuestionRepository questionRepository = new QuestionRepository(entityManager);
        questionRepository.updateQuestionById(id, newQuestionDescription, newQuizTopic);
    }
}


