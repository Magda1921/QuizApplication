package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.model.Result;

import java.util.List;

public class ResultRepository {
    private final EntityManager entityManager;

    public ResultRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void saveNewResult(Result result) {

        entityManager.getTransaction().begin();
        entityManager.persist(result);
        entityManager.getTransaction().commit();
    }

    public List<Result> getAllResultsFromDB() {
        List<Result> results = entityManager.createQuery("select result from Result result").getResultList();
        return results;
    }

    public List<Result> getListOfResultsByStudentNameFromDB(String studentName) {
        List<Result> results = entityManager.createQuery("select result from Result result join Student student on result.student.id = student.id where student.name = ?1", Result.class).setParameter(1, studentName)
                .getResultList();
        return results;
    }
}
