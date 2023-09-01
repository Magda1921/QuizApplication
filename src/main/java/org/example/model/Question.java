package org.example.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Question {

    @Id
    private int id;
    @Column
    private String question;

    @Column
    private String quizTopic;

    public Question() {
    }

    public Question(int id, String question, String quizTopic) {
        this.id = id;
        this.question = question;
        this.quizTopic = quizTopic;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question1 = (Question) o;
        return id == question1.id && Objects.equals(question, question1.question) && Objects.equals(quizTopic, question1.quizTopic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, question, quizTopic);
    }
}



