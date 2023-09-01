package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.model.Question;

public class QuestionRepository {
    private final EntityManager entityManager;

    public QuestionRepository(EntityManager entityManager) {

        this.entityManager = entityManager;
    }

    public void saveNewQuestion(Question question) {

        entityManager.getTransaction().begin();
        Question newQuestion = new Question();
        entityManager.persist(newQuestion);
        entityManager.getTransaction().commit();
    }

    public void removeQuestion(Question question) {
        entityManager.getTransaction().begin();
        entityManager.remove(question);
        entityManager.getTransaction().commit();
    }


}
