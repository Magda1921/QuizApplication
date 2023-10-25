package org.example;

import jakarta.persistence.EntityManager;
import org.example.repository.AnswerRepository;
import org.example.repository.QuestionRepository;
import org.example.repository.ResultRepository;
import org.example.repository.StudentRepository;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        EntityManagerProvider entityManagerProvider = new EntityManagerProvider();
        EntityManager entityManager = entityManagerProvider.getEntityManager();

        AnswerRepository answerRepository = new AnswerRepository(entityManager);
        QuestionRepository questionRepository = new QuestionRepository(entityManager);
        ResultRepository resultRepository = new ResultRepository(entityManager);
        StudentRepository studentRepository = new StudentRepository(entityManager);

        StudentService studentService = new StudentService(studentRepository);
        TeacherService teacherService = new TeacherService(questionRepository, answerRepository, resultRepository);
        QuizService quizService = new QuizService(studentService, questionRepository, resultRepository);

        TeacherConsole teacherConsole = new TeacherConsole(teacherService, studentService);
        StudentConsole studentConsole = new StudentConsole(studentService, quizService, scanner);
        Quiz quiz = new Quiz(teacherConsole, studentConsole, scanner);
        quiz.start();
    }
}