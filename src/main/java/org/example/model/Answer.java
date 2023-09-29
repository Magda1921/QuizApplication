package org.example.model;

import jakarta.persistence.*;

import java.util.Objects;


@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String answer;

    @Column
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "questionId")
    private Question question;

    public Answer() {
    }

    public Answer(String answer, boolean isCorrect, Question question) {
        this.answer = answer;
        this.isCorrect = isCorrect;
        this.question = question;
    }

    public Answer(String answer, boolean isCorrect) {
        this.answer = answer;
        this.isCorrect = isCorrect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer1 = (Answer) o;
        return id == answer1.id && isCorrect == answer1.isCorrect && Objects.equals(answer, answer1.answer) && Objects.equals(question, answer1.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, answer, isCorrect, question);
    }
}
