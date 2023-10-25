package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerProvider {

    private final EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;

    public EntityManagerProvider() {
        entityManagerFactory = Persistence.createEntityManagerFactory("persistenceUnit");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void close() {
        entityManager.close();
        entityManagerFactory.close();
    }
}
