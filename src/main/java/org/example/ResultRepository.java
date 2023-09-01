package org.example;

import jakarta.persistence.EntityManager;
import org.example.model.Result;
import org.example.model.Student;

public class ResultRepository {

    private final EntityManager entityManager;

    public ResultRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public void saveNewResult (Result result) {

        entityManager.getTransaction().begin();
        entityManager.persist(result);
        entityManager.getTransaction().commit();


    }
}
