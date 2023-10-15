package org.example;

import org.example.model.Result;
import org.example.model.Student;
import org.example.repository.ResultRepository;
import org.example.repository.StudentRepository;

import java.util.List;
import java.util.Scanner;

public class TeacherConsole {
    Scanner scanner = new Scanner(System.in);

    public static final int ADD_A_NEW_QUESTIONANewQuestion = 1;
    public static final int REMOVE_QUESTION = 2;
    public static final int SHOW_ALL_QUESTIONS = 3;
    public static final int FIND_QUESTION_BY_PART_OF_THE_DESCRIPTION = 4;
    public static final int UPDATE_QUESTION_BY_ID = 5;
    public static final int SHOW_ALL_RESULTS = 6;
    public static final int FIND_RESULT_BY_STUDENT_NAME = 7;
    public static final int SHOW_ALL_STUDENTS = 8;

    private TeacherService teacherService;
    private StudentService studentService;

    public TeacherConsole(TeacherService teacherService, StudentService studentService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    public void run() {
        int userSelection = -1;
        Scanner scanner = new Scanner(System.in);
        while (userSelection != 0) {
            System.out.println("Please select one of option:");
            System.out.println("0 - exit program");
            System.out.println("1 - add a new question");
            System.out.println("2 - remove question");
            System.out.println("3 - show all questions");
            System.out.println("4 - find question by part of the description");
            System.out.println("5 - update question by id");
            System.out.println("6 - show all results");
            System.out.println("7 - find result by student name");
            System.out.println("8 - show all students");

            userSelection = Integer.parseInt(scanner.nextLine());
            if (userSelection == ADD_A_NEW_QUESTIONANewQuestion) {
                teacherService.addNewQuestion();
            } else if (userSelection == REMOVE_QUESTION) {
                System.out.println("Enter the question id you want to delete");
                int id = scanner.nextInt();
                teacherService.removeQuestion(id);
            } else if (userSelection == SHOW_ALL_QUESTIONS) {
                teacherService.showAllQuestions();
            } else if (userSelection == FIND_QUESTION_BY_PART_OF_THE_DESCRIPTION) {
                teacherService.showQuestionsByPartOfDescription();
            } else if (userSelection == UPDATE_QUESTION_BY_ID) {
                teacherService.updateQuestionByProvidedId();
            } else if (userSelection == SHOW_ALL_RESULTS) {
                teacherService.showAllResults();
            } else if (userSelection == FIND_RESULT_BY_STUDENT_NAME) {
                teacherService.showResultByStudentName();
            } else if (userSelection == SHOW_ALL_STUDENTS) {
                studentService.showAllStudents();
            }
        }
    }


}


