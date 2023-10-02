package org.example;

import java.util.List;
import java.util.Scanner;

public class Quiz {

    public void start() {
        String userChoice;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Are you a teacher or a student? Please write teacher/student in a console.");
        userChoice = scanner.nextLine();
        if (userChoice.equals("teacher")) {
            teacherConsole();
        }
        else if (userChoice.equals("student")) {
            studentConsole();
        }
        else {
            System.out.println(userChoice + " is not valid.");
        }
    }
    public void teacherConsole () {

        TeacherConsole teacherConsole = new TeacherConsole();
        int number = -1;
        Scanner scanner = new Scanner(System.in);
        while (number != 0) {
            System.out.println("Please select one of option:");
            System.out.println("0 - exit program");
            System.out.println("1 - add a new test");
            System.out.println("2 - remove question");
            System.out.println("3 - show all");
            System.out.println("4 - find question by name");
            System.out.println("5 - update question by name");
            number = scanner.nextInt();
            if (number == 1) {
                System.out.println("User selected one");
//                teacherConsole.addNewTest();
            } else if (number == 2) {
                System.out.println("User selected two");

            } else if (number == 3) {
                System.out.println("User selected three");

            } else if (number == 4) {
                System.out.println("Write the name of the question that you want to find");

            } else if (number == 5) {
                System.out.println("Write the id number of the question that you want to update");

            }
        }
    }
    public void studentConsole () {
        int number = -1;
        Scanner scanner = new Scanner(System.in);
        while (number != 0) {
            System.out.println("Please select one of option:");
            System.out.println("0 - exit program");
            System.out.println("1 - choose the quiz by topic");
            System.out.println("2 - ");
            System.out.println("3 - show list of available quizzes");
            System.out.println("4 - find question by name");
            System.out.println("5 - ");
            number = scanner.nextInt();
            if (number == 1) {
                System.out.println("User selected one");

            } else if (number == 2) {
                System.out.println("User selected two");

            } else if (number == 3) {
                System.out.println("User selected three");

            } else if (number == 4) {
                System.out.println("Write the name of the question that you want to find");

            } else if (number == 5) {
                System.out.println("e");

            }
        }
    }
    }

