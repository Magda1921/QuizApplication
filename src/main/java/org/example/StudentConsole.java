package org.example;

import jakarta.persistence.EntityManager;
import org.example.model.Answer;
import org.example.model.Question;
import org.example.model.Result;
import org.example.model.Student;

import java.util.*;
import java.util.stream.Collectors;

public class StudentConsole {

    Scanner scanner = new Scanner(System.in);

    private EntityManager entityManager;
    Quiz quiz = new Quiz();

    public StudentConsole(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public void addNewStudent() {

        Scanner scanner = new Scanner(System.in);
        String studentName;

        System.out.println("Enter your name");
        studentName = scanner.nextLine();

        Student student = new Student();
        student.setName(studentName);
        StudentRepository studentRepository = new StudentRepository(entityManager);
        studentRepository.saveNewStudent(student);
    }


    public void play(QuestionRepository questionRepository) {
        System.out.println("Enter the quiz topic you want to play");
        String quizTopic = scanner.nextLine();

        AccountOperations accountOperations = new AccountOperations();
        Student activeStudent = accountOperations.checkIfAccountExist();

        List<Question> questions = questionRepository.findQuestionsbyQuizTopic(quizTopic);

        List<Question> quizQuestions = questionRepository.randomQuestions(questions);

        String corectAnswer = "";
        Integer numberOfCorrectAnswers = 0;
        Integer numberOfQuestions = 0;

        for (Question question : quizQuestions) {
            numberOfQuestions++;
            System.out.println(question.getQuestion());
            List<Answer> answers = question.getAnswers();
            List<String> abcd = new ArrayList<>();
            abcd.add("a");
            abcd.add("b");
            abcd.add("c");
            abcd.add("d");
            for (int i = 0; i < 4; i++) {
                HashMap<String, String> answerHashMap = new HashMap<String, String>();
                answerHashMap.put(answers.get(i).getAnswer(), abcd.get(i));
                System.out.println(answerHashMap);

                if (answers.get(i).isCorrect()) {
                    corectAnswer = answerHashMap.get(answers.get(i).getAnswer());
                }
            }
            System.out.println("Enter the right answer (a, b, c or d): ");
            String answerFromUser = scanner.nextLine();
            if (answerFromUser.equals(corectAnswer)) {
                numberOfCorrectAnswers++;
            }
        }
        addNewResult(numberOfQuestions, numberOfCorrectAnswers, quizTopic, activeStudent);
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
