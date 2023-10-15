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

            int addANewQuestion = 1;
            int removeQuestion = 2;
            int showAllQuestions = 3;
            int findQuestionByPartOfTheDescription = 4;
            int updateQuestionById = 5;
            int showAllResults = 6;
            int findResultByStudentName = 7;
            int showAllStudents = 8;

            userSelection = Integer.parseInt(scanner.nextLine());
            QuestionRepository questionRepository = new QuestionRepository(entityManager);
            if (userSelection == addANewQuestion) {
                System.out.println("User selected one");
                teacherConsole.addNewQuestion();
            } else if (userSelection == removeQuestion) {
                System.out.println("User selected two");
                teacherConsole.removeQuestion();
            } else if (userSelection == showAllQuestions) {
                System.out.println("User selected three");
                List<Question> questions = questionRepository.getAllQuestions();
                for (Question q : questions) {
                    System.out.println("Id: " + q.getId());
                    System.out.println("Question: " + q.getQuestion());
                    System.out.println("Quiz topic: " + q.getQuizTopic());
                }
            } else if (userSelection == findQuestionByPartOfTheDescription) {
                System.out.println("Write the part of the description of the question that you want to find");
                String partOfDescription = scanner.nextLine();
                List<Question> questions = questionRepository.findQuestionByPartOfName(partOfDescription);
                for (Question question : questions) {
                    System.out.println(question.getQuestion());
                }
            } else if (userSelection == updateQuestionById) {
                System.out.println("Write the id of the question that you want to update");
                int idToUpdate = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter the new quiz topic: ");
                String newQuizTopic = scanner.nextLine();
                System.out.println("Enter the new question description");
                String newQuestionDescription = scanner.nextLine();
                teacherConsole.updateQuestionByProvidedId(idToUpdate, newQuestionDescription, newQuizTopic);
            } else if (userSelection == showAllResults) {
                ResultRepository resultRepository = new ResultRepository(entityManager);
                List<Result> results = resultRepository.getAllResultsFromDB();
                for (Result result : results) {
                    System.out.println("Result Id : " + result.getId());
                    System.out.println("Result : " + result.getResult());
                    System.out.println("Quiz topic : " + result.getQuizTopic());
                    System.out.println("Student name: " + result.getStudent().getName());
                }
            } else if (userSelection == findResultByStudentName) {
                System.out.println("Write the name of the student");
                String studentName = scanner.nextLine();
                ResultRepository resultRepository = new ResultRepository(entityManager);
                List<Result> results = resultRepository.getListOfResultsByStudentNameFromDB(studentName);
                for (Result result : results) {
                    System.out.println("Student name = " + result.getStudent().getName() + " Result = " + result.getResult() + " Quiz topic = " + result.getQuizTopic());
                }
            } else if (userSelection == showAllStudents) {
                StudentRepository studentRepository = new StudentRepository(entityManager);
                List<Student> students = studentRepository.getListOfStudentsFromDB();
                for (Student student : students) {
                    System.out.println("Student name: " + student.getName());
                }
            }
        }
    }

    public void studentConsole() {
        StudentConsole studentConsole = new StudentConsole(entityManager);
        QuestionRepository questionRepository = new QuestionRepository(entityManager);
        int userSelection = -1;
        Scanner scanner = new Scanner(System.in);
        while (userSelection != 0) {
            System.out.println("Please select one of option:");
            System.out.println("0 - exit program");
            System.out.println("1 - choose the quiz by topic and play");
            System.out.println("2 - add your account");
            System.out.println("3 - show list of available quizzes");
            System.out.println("4 - find question by description");

            int chooseTheQuizByTopicAndPlay = 1;
            int addYourAccount = 2;
            int showListOfAvailableQuizzes = 3;
            int findQuestionByDescription = 4;

            userSelection = scanner.nextInt();
            if (userSelection == chooseTheQuizByTopicAndPlay) {
                System.out.println("User selected one");
                studentConsole.play(questionRepository);
            } else if (userSelection == addYourAccount) {
                System.out.println("User selected two");
                Student student = studentConsole.addNewStudent();
                studentConsole.saveNewStudent(student);
            } else if (userSelection == showListOfAvailableQuizzes) {
                System.out.println("User selected three");
                List<Question> questions = questionRepository.getAllQuestions();
                questions.stream()
                        .map(Question::getQuizTopic)
                        .distinct()
                        .forEach(System.out::println);
            } else if (userSelection == findQuestionByDescription) {
                System.out.println("Write the part of the description of the question that you want to find");
                scanner.nextLine();
                String partOfDescription = scanner.nextLine();
                List<Question> questions = questionRepository.findQuestionByPartOfName(partOfDescription);
                for (Question question : questions) {
                    System.out.println(question.getQuestion());
                }
            }
        }
    }
}

