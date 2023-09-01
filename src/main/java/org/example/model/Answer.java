package org.example.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Answer {
    @Id
    private int id;

    @Column
    private String a;

    @Column
    private String b;

    @Column
    private String c;

    @Column
    private String d;

    @Column
    private String rightAnswer;

    @OneToOne
    @JoinColumn (name = "questionId")
    private Question question;

    public Answer() {
    }

    public Answer(int id, String a, String b, String c, String d, String rightAnswer, Question question) {
        this.id = id;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.rightAnswer = rightAnswer;
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
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
        Answer answer = (Answer) o;
        return id == answer.id && Objects.equals(a, answer.a) && Objects.equals(b, answer.b) && Objects.equals(c, answer.c) && Objects.equals(d, answer.d) && Objects.equals(rightAnswer, answer.rightAnswer) && Objects.equals(question, answer.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, a, b, c, d, rightAnswer, question);
    }
}
