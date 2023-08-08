package org.example;

public class Main {


    public static void main(String[] args) {
        QuestionRepository repo = new QuestionRepository();
        repo.createNewQuestion(0, "Pierwsze pytanie", "dobra odpowiedz", "zla odpowiedz", "zla odpowiedz 2", "zla odpowiedz 3");
       Quiz quiz = new Quiz();
       quiz.start();
    }
}