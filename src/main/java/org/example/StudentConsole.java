package org.example;

import jakarta.persistence.EntityManager;
import org.example.model.Question;
import org.example.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class StudentConsole {

    Scanner scanner = new Scanner(System.in);
    Student student = new Student();
    private EntityManager entityManager;

    public StudentConsole(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    ResultRepository resultRepository = new ResultRepository(entityManager);

    public void addNewStudent() {
        Scanner scanner = new Scanner(System.in);
        List<Student> students = new ArrayList<>();
        String studentName;

        System.out.println("Enter your name");
        studentName = scanner.nextLine();

        student.setName(studentName);
        StudentRepository studentRepository = new StudentRepository(entityManager);
        studentRepository.saveNewStudent(student);
    }

    public List<Question> findQuestionsbyQuizTopic(String quizTopic) {
        List<Question> questions = entityManager.createQuery("select question from Question question where question.quizTopic = ?1").setParameter(1, quizTopic).getResultList();
        return questions;
    }

    public void play(List<Question> questions) {
        Random random = new Random();
        Question question1 = questions.get(random.nextInt(questions.size()));
        Question question2 = questions.get(random.nextInt(questions.size()));
        List<Question> quizQuestion = new ArrayList<>();
        quizQuestion.add(question1);
        quizQuestion.add(question2);

        questions.stream()
                .map(Question::getQuestion)
                .forEach(System.out::println);

        for (Question question : quizQuestion) {
            System.out.println(question.getQuestion());
//            System.out.println("a: " + question.getRightAnswer());
//            System.out.println("b: " + question.getWrongAnswer1());
//            System.out.println("c: " + question.getWrongAnswer2());
//            System.out.println("d: " + question.getWrongAnswer3());
            System.out.println("Enter the right answer (a, b, c or d): ");
            String answer = scanner.nextLine();
        }
    }

    public List<Question> showQuiz() {
        List<Question> questions = entityManager.createQuery("select question from Question question").getResultList();
        return questions;
    }
}
