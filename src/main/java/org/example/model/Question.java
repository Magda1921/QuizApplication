package org.example.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String question;

    @Column
    private String quizTopic;


    @OneToMany(mappedBy = "question", cascade = {CascadeType.ALL})
    private List<Answer> answers;

    public Question() {
    }

    public Question(int id, String question, String quizTopic, List<Answer> answers) {
        this.id = id;
        this.question = question;
        this.quizTopic = quizTopic;
        this.answers = answers;
    }

    public Question(String question, String quizTopic, List<Answer> answers) {
        this.question = question;
        this.quizTopic = quizTopic;
        this.answers = answers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuizTopic() {
        return quizTopic;
    }

    public void setQuizTopic(String quizTopic) {
        this.quizTopic = quizTopic;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question1 = (Question) o;
        return id == question1.id && Objects.equals(question, question1.question) && Objects.equals(quizTopic, question1.quizTopic) && Objects.equals(answers, question1.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, question, quizTopic, answers);
    }
}



