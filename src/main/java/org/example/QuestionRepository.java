package org.example;

import jakarta.persistence.EntityManager;
import org.example.model.Question;

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

        Question question = (Question) entityManager.createQuery("select question from Question question where question.question = ?1")
                .setParameter(1, questionDescription)
                .getSingleResult();
        return question;

    }

    public List<Question> findQuestionByPartOfName(String partOfDescription) {
        String searchString = "%" + partOfDescription + "%";
        List<Question> questions = entityManager.createQuery("select question from Question question where question.question like :searchString")
                .setParameter("searchString", searchString)
                .getResultList();
        return questions;
    }

    public List<Question> randomQuestions(List<Question> questions) {
        Random random = new Random();
        Question question1 = questions.get(random.nextInt(questions.size()));
        questions.remove(question1);
        Question question2 = questions.get(random.nextInt(questions.size()));
        List<Question> quizQuestions = new ArrayList<>();
        quizQuestions.add(question1);
        quizQuestions.add(question2);

        return quizQuestions;
    }
}
