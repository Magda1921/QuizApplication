package org.example.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Student {
    @Id
    private int id;
    @Column

    private String name;
    @OneToMany(mappedBy = "student", cascade = {CascadeType.ALL})
    private List<Result> results;

    public Student(int studentId, String name, List<Result> results) {
        this.id = studentId;
        this.name = name;
        this.results = results;
    }

    public Student(String name) {
        this.name = name;
    }

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Student() {
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public void setId(int student_id) {
        this.id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id && Objects.equals(name, student.name) && Objects.equals(results, student.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, results);
    }
}
