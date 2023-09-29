package org.example;

import jakarta.persistence.EntityManager;
import org.example.model.Answer;

public class AnswerRepository {

    private EntityManager entityManager;

    public AnswerRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void saveNewAnswer(Answer answer) {
        entityManager.getTransaction().begin();
        entityManager.persist(answer);
        entityManager.getTransaction().commit();
    }

    public void removeAnswer(Answer answer) {
        entityManager.getTransaction().begin();
        entityManager.remove(answer);
        entityManager.getTransaction().commit();
    }
}
