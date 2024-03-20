package com.company.carpooling.repositories;

import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.models.ActivationCode;
import com.company.carpooling.repositories.contracts.ActivationCodeRepository;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class ActivationCodeRepositoryImpl implements ActivationCodeRepository {
    private final SessionFactory sessionFactory;

    @Override
    public ActivationCode getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<ActivationCode> query = session.createQuery(
                    "from ActivationCode where email = :email", ActivationCode.class);
            query.setParameter("email", email);

            List<ActivationCode> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Code", "email", email);
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
    public void deleteActivationCodeByUser(String email) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Query<ActivationCode> query = session.createQuery(
                    "FROM ActivationCode WHERE email = :email", ActivationCode.class);
            query.setParameter("email", email);
            List<ActivationCode> activationCodes = query.getResultList();
            for (ActivationCode activationCode : activationCodes) {
                session.remove(activationCode);
            }
            session.getTransaction().commit();
        }
    }
}
