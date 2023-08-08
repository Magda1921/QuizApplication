package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.model.Question;

public class QuestionRepository {
    public void createNewQuestion(int id, String question, String rightAnswer, String wrongAnswer1, String wrongAnswer2, String wrongAnswer3) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        Insert data
         entityManager.getTransaction().begin();
        Question newOne = new Question(id, question, rightAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3);
        entityManager.persist(newOne);
        entityManager.getTransaction().commit();
    }
}
