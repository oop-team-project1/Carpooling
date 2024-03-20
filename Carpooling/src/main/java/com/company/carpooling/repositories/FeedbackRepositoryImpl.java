package com.company.carpooling.repositories;

import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.models.Feedback;
import com.company.carpooling.models.FeedbackComment;
import com.company.carpooling.repositories.contracts.FeedbackRepository;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class FeedbackRepositoryImpl implements FeedbackRepository {
    private final SessionFactory sessionFactory;

    @Override
    public List<Feedback> get(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Feedback> query = session.createQuery("from Feedback  where " +
                    " receiver.id = :id ", Feedback.class);
            query.setParameter("id", userId);

            List<Feedback> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException(String.format("No feedbacks for user with id %d.", userId));
            }
            return result;
        }
    }

    @Override
    public Feedback getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Feedback feedback = session.get(Feedback.class, id);
            if (feedback == null) {
                throw new EntityNotFoundException("Feedback", id);
            }
            return feedback;
        }
    }

    @Override
    public FeedbackComment getCommentById(int id) {
        try (Session session = sessionFactory.openSession()) {
            FeedbackComment comment = session.get(FeedbackComment.class, id);
            if (comment == null) {
                throw new EntityNotFoundException("Comment for feedback", id);
            }
            return comment;
        }
    }

    @Override
    public void create(Feedback feedback) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(feedback);
            session.getTransaction().commit();
        }
    }

    @Override
    public void createFeedbackComment(FeedbackComment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Feedback feedback) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(feedback);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteFeedbackComment(FeedbackComment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(comment);
            session.getTransaction().commit();
        }
    }
}
