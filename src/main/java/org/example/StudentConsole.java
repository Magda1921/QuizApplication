package org.example;

import jakarta.persistence.EntityManager;
import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Result;
import org.example.model.Student;

import java.util.*;

public class StudentConsole {

    Scanner scanner = new Scanner(System.in);

    private EntityManager entityManager;

    public StudentConsole(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public Student addNewStudent() {
        Scanner scanner = new Scanner(System.in);
        String studentName;

        System.out.println("Enter your name");
        studentName = scanner.nextLine();

        Student student = new Student();
        student.setName(studentName);

        return student;
    }

    public void saveNewStudent(Student student) {

        StudentRepository studentRepository = new StudentRepository(entityManager);
        studentRepository.saveNewStudent(student);
    }

    public void play(QuestionRepository questionRepository) {
        System.out.println("Enter the quiz topic you want to play");
        String quizTopic = scanner.nextLine();
        try {
            Student activeStudent = getExistingAccount();
            List<Question> questions = questionRepository.findQuestionsbyQuizTopic(quizTopic);
            List<Question> quizQuestions = questionRepository.randomQuestions(questions);

            String corectAnswer = "";
            Integer numberOfCorrectAnswers = 0;
            Integer numberOfQuestions = 0;

            for (Question question : quizQuestions) {
                numberOfQuestions++;
                displayQuestion(question);
                List<Answer> answers = question.getAnswers();
                List<String> abcd = new ArrayList<>();
                abcd.add("a");
                abcd.add("b");
                abcd.add("c");
                abcd.add("d");
                for (int i = 0; i < 4; i++) {
                    HashMap<String, String> answerHashMap = new HashMap<String, String>();
                    answerHashMap.put(answers.get(i).getAnswer(), abcd.get(i));
                    displayAnswer(answerHashMap);

                    if (answers.get(i).isCorrect()) {
                        corectAnswer = answerHashMap.get(answers.get(i).getAnswer());
                    }
                }
                System.out.println("Enter the right answer (a, b, c or d): ");
                String answerFromUser = scanner.nextLine();
                numberOfCorrectAnswers = countNumberOfCorrectAnswers(answerFromUser, corectAnswer, numberOfCorrectAnswers);
            }

            addNewResult(numberOfQuestions, numberOfCorrectAnswers, quizTopic, activeStudent);
        }
        catch (RuntimeException e) {
            System.out.println("Please try play quiz again or contact support IT");
        }
    }

    public int countNumberOfCorrectAnswers(String answerFromUser, String correctAnswer, int numberOfCorrectAnswers) {
        if (answerFromUser.equals(correctAnswer)) {
            numberOfCorrectAnswers++;
        }
        return numberOfCorrectAnswers;
    }

    public void displayQuestion(Question question) {
        System.out.println(question.getQuestion());
    }

    public void displayAnswer(HashMap<String, String> answerHashMap) {
        System.out.println(answerHashMap);
    }

    public Student getExistingAccount() {

        System.out.println("Do you have an account? Please enter true/false");
        Scanner scanner = new Scanner(System.in);
        boolean accountIsActive = scanner.nextBoolean();
        Student activeStudent = new Student();

        if (accountIsActive) {
            scanner.nextLine();
            System.out.println("Please enter your name");
            String studentName = scanner.nextLine();
            activeStudent = findStudentByStudentName(studentName);
        } else if (!accountIsActive) {
            System.out.println("Please add a new account");
            activeStudent = addNewStudent();
            saveNewStudent(activeStudent);
        }
        return activeStudent;
    }

    public List<Question> showQuiz() {
        List<Question> questions = entityManager.createQuery("select question from Question question").getResultList();
        return questions;
    }

    public void addNewResult(int numberOfQuestions, int numberOfRightAnswers, String quizTopic, Student activeStudent) {
        int finalResult = ((numberOfRightAnswers / numberOfQuestions) * 100);
        Result result = new Result();
        result.setResult(finalResult);
        result.setStudent(activeStudent);
        result.setQuizTopic(quizTopic);

        ResultRepository resultRepository = new ResultRepository(entityManager);
        resultRepository.saveNewResult(result);

    }

    public Student findStudentByStudentName(String studentName) {

        List<Student> students = entityManager.createQuery("select student from Student student where student.name = ?1")
                .setParameter(1, studentName)
                .getResultList();
        Student student = new Student();
        if (students.size() > 1) {
            System.out.println("There is more than 1 student with " + studentName + " name. Please provide us with your student id");
            int studentId = scanner.nextInt();
            student = (Student) entityManager.createQuery("select student from Student student where student.id = ?1")
                    .setParameter(1, studentId)
                    .getSingleResult();
        } else if (students.size() == 1) {
            student = students.get(0);

        }
        return student;
    }
}
