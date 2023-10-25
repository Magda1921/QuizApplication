package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.model.Question;
import org.hibernate.SessionFactory;

import javax.security.auth.login.Configuration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionRepository {
    private EntityManager entityManager;

    public QuestionRepository(EntityManager entityManager) {

        this.entityManager = entityManager;
    }

    public void saveNewQuestion(Question question) {

        entityManager.getTransaction().begin();
        entityManager.persist(question);
        entityManager.getTransaction().commit();
    }

    public void removeQuestion(Question question) {
        entityManager.getTransaction().begin();
        entityManager.remove(question);
        entityManager.getTransaction().commit();
    }

    public List<Question> findQuestionsbyQuizTopic(String quizTopic) {

        List<Question> questions = entityManager.createQuery("select question from Question question where question.quizTopic = ?1").setParameter(1, quizTopic).getResultList();
        return questions;
    }

    public Question findQuestionByDescription(String questionDescription) {
        try {
            Question question = (Question) entityManager.createQuery("select question from Question question where question.question = ?1")
                    .setParameter(1, questionDescription)
                    .getSingleResult();
            return question;
        } catch (NoResultException e) {
            System.out.println("Cannot find question with " + questionDescription);
        }
        return null;
    }

        public List<Question> findQuestionByPartOfName (String partOfDescription){
            String searchString = "%" + partOfDescription + "%";
            List<Question> questions = entityManager.createQuery("select question from Question question where question.question like :searchString")
                    .setParameter("searchString", searchString)
                    .getResultList();
            return questions;
        }

        public List<Question> getAllQuestions () {
            List<Question> questions = entityManager.createQuery("select question from Question question").getResultList();
            return questions;
        }

        public void updateQuestionById ( int questionId, String questionDescription, String questionTopic){

            entityManager.getTransaction().begin();

            Question questionToUpdate = entityManager.find(Question.class, questionId);
            if (questionToUpdate != null) {
                questionToUpdate.setQuestion(questionDescription);
                questionToUpdate.setQuizTopic(questionTopic);
            } else {
                System.out.println("There is no question with questionId: " + questionId);
            }
            entityManager.getTransaction().commit();
        }
        public Question findQuestionById ( int id){
            Question question = entityManager.find(Question.class, id);
            return question;
        }
    }

