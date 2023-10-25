package org.example;

import jakarta.persistence.EntityManager;
import org.example.model.Question;
import org.example.model.Result;
import org.example.model.Student;
import org.example.repository.QuestionRepository;
import org.example.repository.ResultRepository;
import org.example.repository.StudentRepository;

import java.util.List;
import java.util.Scanner;

public class Quiz {

    private TeacherConsole teacherConsole;
    private StudentConsole studentConsole;
    private Scanner scanner;

    public Quiz(TeacherConsole teacherConsole, StudentConsole studentConsole, Scanner scanner) {
        this.teacherConsole = teacherConsole;
        this.studentConsole = studentConsole;
        this.scanner = scanner;
    }

    public void start() {
        String userChoice;
        System.out.println("Are you a teacher or a student? Please write teacher/student in a console.");
        userChoice = scanner.nextLine();
        if (userChoice.equals("teacher")) {
            teacherConsole.run();
        } else if (userChoice.equals("student")) {
            studentConsole.run();
        } else {
            System.out.println(userChoice + " is not valid.");
        }
    }
}

