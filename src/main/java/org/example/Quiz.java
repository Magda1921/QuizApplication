package org.example;

import jakarta.persistence.EntityManager;
import org.example.model.Question;
import org.example.model.Result;

import java.util.List;
import java.util.Scanner;

public class Quiz {
    private EntityManager entityManager;


    public void start() {
        EntityManagerProvider entityManagerProvider = new EntityManagerProvider();
        entityManager = entityManagerProvider.getEntityManager();
        String userChoice;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Are you a teacher or a student? Please write teacher/student in a console.");
        userChoice = scanner.nextLine();
        if (userChoice.equals("teacher")) {
            teacherConsole();
        } else if (userChoice.equals("student")) {
            studentConsole();
        } else {
            System.out.println(userChoice + " is not valid.");
        }
    }

    public void teacherConsole() {

        TeacherConsole teacherConsole = new TeacherConsole(entityManager);
        int number = -1;
        Scanner scanner = new Scanner(System.in);
        while (number != 0) {
            System.out.println("Please select one of option:");
            System.out.println("0 - exit program");
            System.out.println("1 - add a new question");
            System.out.println("2 - remove question");
            System.out.println("3 - show all questions");
            System.out.println("4 - find question by id");
            System.out.println("5 - update question by id");
            System.out.println("6 - show all results");
            System.out.println("7 - find result by student name");

            number = Integer.parseInt(scanner.nextLine());

            if (number == 1) {
                System.out.println("User selected one");
                teacherConsole.addNewQuestion();
            } else if (number == 2) {
                System.out.println("User selected two");
                teacherConsole.removeQuestion();
            } else if (number == 3) {
                System.out.println("User selected three");
                List<Question> questions = teacherConsole.showAllQuestions();
                for (Question q : questions) {
                    System.out.println("Id: " + q.getId());
                    System.out.println("Question: " + q.getQuestion());
                    System.out.println("Quiz topic: " + q.getQuizTopic());
                }
            } else if (number == 4) {
                System.out.println("Write the id of the question that you want to find");
                int wantedId = scanner.nextInt();
                Question foundQuestionById = teacherConsole.findQuestionById(wantedId);
                System.out.println(foundQuestionById.getId());
                System.out.println(foundQuestionById.getQuestion());
                System.out.println(foundQuestionById.getQuizTopic());
            } else if (number == 5) {
                System.out.println("Write the id number of the question that you want to update");
                int idToUpdate = scanner.nextInt();
                scanner.nextLine();
                teacherConsole.updateQuestionById(idToUpdate);
            } else if (number == 6) {
                List<Result> results = teacherConsole.showAllResults();
                for (Result result : results) {
                    System.out.println("Result Id : " + result.getId());
                    System.out.println("Result : " + result.getResult());
                    System.out.println("Quiz topic : " + result.getQuizTopic());
                    System.out.println("Student name: " + result.getStudent().getName());
                }
            } else if (number == 7) {
                System.out.println("Write the name of the student");
                scanner.nextLine();
                String studentName = scanner.nextLine();
                List<Result> results = teacherConsole.findResultByStudentName(studentName);
                for (Result result : results) {
                    System.out.println("Result id = " + result.getId());
                    System.out.println("Result = " + result.getResult());
                    System.out.println("Quiz Topic = " + result.getQuizTopic());
                    System.out.println("Student name = " + result.getStudent().getName());
                }
            }
        }
    }

    public void studentConsole() {
        StudentConsole studentConsole = new StudentConsole(entityManager);
        int number = -1;
        Scanner scanner = new Scanner(System.in);
        while (number != 0) {
            System.out.println("Please select one of option:");
            System.out.println("0 - exit program");
            System.out.println("1 - choose the quiz by topic and play");
            System.out.println("2 - add your account");
            System.out.println("3 - show list of available quizzes");
            System.out.println("4 - find question by id");
            number = scanner.nextInt();
            if (number == 1) {
                System.out.println("User selected one");
                System.out.println("Enter the quiz topic you want to play");
                scanner.nextLine();
                String quizTopic = scanner.nextLine();
                List<Question> questions = studentConsole.findQuestionsbyQuizTopic(quizTopic);
                studentConsole.play(questions);
            } else if (number == 2) {
                System.out.println("User selected two");
                studentConsole.addNewStudent();
            } else if (number == 3) {
                System.out.println("User selected three");
                List<Question> questions = studentConsole.showQuiz();
                questions.stream()
                        .map(Question::getQuizTopic)
                        .forEach(System.out::println);
            } else if (number == 4) {
                System.out.println("Write the id of the question that you want to find");

            }
        }
    }
}

