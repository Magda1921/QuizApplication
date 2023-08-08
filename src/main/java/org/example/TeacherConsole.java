package org.example;

import org.example.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TeacherConsole {
    public void addNewQuiz () {
        QuizPackage quizPackage = new QuizPackage();
        Scanner scanner = new Scanner(System.in);
        String nameOfQuizPackage;
        System.out.println("Enter the name of the test: ");
        nameOfQuizPackage = scanner.nextLine();
        quizPackage.setName(nameOfQuizPackage);

        List <Question> questions = addNewQuestion();

    }

    public List <Question> addNewQuestion() {
        Scanner scanner = new Scanner(System.in);
        List <Question> questions = new ArrayList<>();
        int questionId = 0;
        String questionDescription = null;
        String rightAnswer = null;
        String wrongAnswer1 = null;
        String wrongAnswer2 = null;
        String wrongAnswer3 = null;

        while (true) {
            System.out.println("Enter next question or click x for exit");
            questionDescription = scanner.nextLine();
            if (questionDescription.equals("x")) {
                break;
            } else {

                System.out.println("Enter the right answer");
                rightAnswer = scanner.nextLine();
                System.out.println("Enter the first wrong answer");
                wrongAnswer1 = scanner.nextLine();
                System.out.println("Enter the second wrong answer");
                wrongAnswer2 = scanner.nextLine();
                System.out.println("Enter the third wrong answer");
                wrongAnswer3 = scanner.nextLine();

                questionId++;
                Question question = new Question();
                question.setId(questionId);
                question.setQuestion(questionDescription);
                question.setRightAnswer(rightAnswer);
                question.setWrongAnswer1(wrongAnswer1);
                question.setWrongAnswer2(wrongAnswer2);
                question.setWrongAnswer3(wrongAnswer3);
            }
        }
        return questions;
    }
}
