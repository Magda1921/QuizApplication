package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.model.Student;

import java.util.List;

public class StudentRepository {
    private final EntityManager entityManager;

    public StudentRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void saveNewStudent(Student student) {

        entityManager.getTransaction().begin();
        entityManager.persist(student);
        entityManager.getTransaction().commit();
    }

    public List<Student> getListOfStudentsFromDB() {
        List<Student> students = entityManager.createQuery("select student from Student student").getResultList();
        return students;
    }

    public List<Student> getListOfStudentsByStudentNameFromDB(String studentName) {
        List<Student> students = entityManager.createQuery("select student from Student student where student.name = ?1")
                .setParameter(1, studentName)
                .getResultList();
        return students;
    }
    public Student getStudentByStudentIdFromDB (int studentId) {
       Student student = (Student) entityManager.createQuery("select student from Student student where student.id = ?1")
                .setParameter(1, studentId)
                .getSingleResult();
       return student;
    }
}
