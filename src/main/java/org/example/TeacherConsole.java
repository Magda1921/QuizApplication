package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Result;
import org.example.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TeacherConsole {
    Scanner scanner = new Scanner(System.in);
    private EntityManager entityManager;

    public TeacherConsole(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addNewQuestion() {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter next question or click x for exit");
            String questionDescription = scanner.nextLine();
            if (questionDescription.equals("x")) {
                scanner.close();
                break;
            } else {

                System.out.println("Enter the 'a' answer");
                String a = scanner.nextLine();
                System.out.println("Enter the 'b' answer");
                String b = scanner.nextLine();
                System.out.println("Enter the 'c' answer");
                String c = scanner.nextLine();
                System.out.println("Enter the 'd' answer");
                String d = scanner.nextLine();
                System.out.println("Enter the right answer");
                String rightAnswer = scanner.nextLine();
                System.out.println("What topic of the quiz should this question be assigned to?");
                String quizTopic = scanner.nextLine();


                QuestionRepository questionRepository = new QuestionRepository(entityManager);
                Question question = new Question();
                question.setQuestion(questionDescription);
                questionRepository.saveNewQuestion(question);
                question.getId();
                System.out.println(question.getId());
                Answer answer = new Answer();



            }
        }
        scanner.close();
    }

    public void removeQuestion() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the question id you want to delete");
        int id = scanner.nextInt();
        QuestionRepository questionRepository = new QuestionRepository(entityManager);
        Question question = entityManager.find(Question.class, id);
        questionRepository.removeQuestion(question);

    }

    public List<Question> showAllQuestions() {
        List<Question> questions = entityManager.createQuery("select question from Question question").getResultList();
        return questions;
    }

    public Question findQuestionById(int id) {
        Question question = (Question) entityManager.createQuery("select question from Question question where question.id = ?1")
                .setParameter(1, id)
                .getSingleResult();
        return question;
    }

    public void updateQuestionById(int id) {
        Question questionToUpdate = entityManager.find(Question.class, id);

        QuestionRepository questionRepository = new QuestionRepository(entityManager);
        questionRepository.removeQuestion(questionToUpdate);

//        System.out.println("Enter the new question: ");
//        String newQuestion = scanner.nextLine();
//        questionToUpdate.setQuestion(newQuestion);
//
//        System.out.println("Enter the new 'a' answer: ");
//        String a = scanner.nextLine();
//        questionToUpdate.setRightAnswer(newRightAnswer);
//
//        System.out.println("Enter the new 'b' answer: ");
//        String b = scanner.nextLine();
//        questionToUpdate.setWrongAnswer1(newWrongAnswer1);
//
//        System.out.println("Enter the new 'c' answer: ");
//        String c = scanner.nextLine();
//        questionToUpdate.setWrongAnswer2(newWrongAnswer2);
//
//        System.out.println("Enter the new 'd' answer: ");
//        String d = scanner.nextLine();
//        questionToUpdate.setWrongAnswer1(newWrongAnswer3);
//
//        System.out.println("Enter the right answer: ");
//        String rightAnswer = scanner.nextLine();


        System.out.println("Enter the new quiz topic: ");
        String newQuizTopic = scanner.nextLine();
        questionToUpdate.setQuizTopic(newQuizTopic);

        questionToUpdate.setId(id);

        questionRepository.saveNewQuestion(questionToUpdate);

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


