package com.company.carpooling.repositories;

import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.models.ActivationCode;
import com.company.carpooling.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActivationCodeRepositoryImpl implements ActivationCodeRepository{
    private final SessionFactory sessionFactory;

    @Autowired
    public ActivationCodeRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public ActivationCode getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<ActivationCode> query = session.createQuery(
                    "from ActivationCode where username = :username", ActivationCode.class);
            query.setParameter("username", username);

            List<ActivationCode> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Code", "username", username);
            }

            return result.get(0);
        }
    }

    @Override
    public ActivationCode getByCode(int code) {
        try (Session session = sessionFactory.openSession()) {
            ActivationCode activationCode = session.get(ActivationCode.class, code);
            if (activationCode == null) {
                throw new EntityNotFoundException("User", code);
            }
            return activationCode;
        }
    }

    @Override
    public void create(ActivationCode activationCode) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(activationCode);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(ActivationCode activationCode) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(activationCode);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteActivationCode(int code) {
        ActivationCode codeToDelete = getByCode(code);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(codeToDelete);
            session.getTransaction().commit();
        }
    }
}
