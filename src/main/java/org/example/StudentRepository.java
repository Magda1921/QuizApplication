package org.example;

import jakarta.persistence.EntityManager;
import org.example.model.Student;

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

}
