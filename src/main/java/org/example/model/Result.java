package org.example.model;

import jakarta.persistence.*;

@Entity
public class Result {

    @Id
    private int id;
    @Column
    private String quizTopic;
    @Column
    private double result;
    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;


    public Result(int id, String quizTopic, int result) {
        this.id = id;
        this.quizTopic = quizTopic;
        this.result = result;
    }

    public Result() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuizTopic() {
        return quizTopic;
    }

    public void setQuizTopic(String quizTopic) {
        this.quizTopic = quizTopic;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
