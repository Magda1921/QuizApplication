package org.example;

import jakarta.persistence.EntityManager;
import org.example.model.Question;
import org.example.model.Student;
import org.example.repository.QuestionRepository;

import java.util.*;

public class StudentConsole {
    public static final int CHOOSE_THE_QUIZ_BY_TOPIC_AND_PLAY = 1;
    public static final int ADD_YOUR_ACCOUNT = 2;
    public static final int SHOW_LIST_OF_AVAILABLE_QUIZZES = 3;
    public static final int FIND_QUESTION_BY_DESCRIPTION = 4;

    private StudentService studentService;
    private QuizService quizService;
    private Scanner scanner;

    public StudentConsole(StudentService studentService, QuizService quizService, Scanner scanner) {
        this.studentService = studentService;
        this.quizService = quizService;
        this.scanner = scanner;
    }

    public void run() {
        int userSelection = -1;
        Scanner scanner = new Scanner(System.in);
        while (userSelection != 0) {
            System.out.println("Please select one of option:");
            System.out.println("0 - exit program");
            System.out.println("1 - choose the quiz by topic and play");
            System.out.println("2 - add your account");
            System.out.println("3 - show list of available quizzes");
            System.out.println("4 - find question by description");
            userSelection = scanner.nextInt();
            if (userSelection == CHOOSE_THE_QUIZ_BY_TOPIC_AND_PLAY) {
                quizService.play();
            } else if (userSelection == ADD_YOUR_ACCOUNT) {
                Student student = studentService.retrieveStudentFromUser();
                studentService.addAndSaveNewStudent(student);
            } else if (userSelection == SHOW_LIST_OF_AVAILABLE_QUIZZES) {
                quizService.showListOfAvailableQuizzes();
            } else if (userSelection == FIND_QUESTION_BY_DESCRIPTION) {
                quizService.findQuestionByPartOfDescription();
            }
        }
    }
}
