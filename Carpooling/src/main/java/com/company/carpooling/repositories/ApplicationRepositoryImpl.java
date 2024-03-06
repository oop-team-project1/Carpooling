package com.company.carpooling.repositories;

import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.models.Application;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.User;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class ApplicationRepositoryImpl implements ApplicationRepository {

    public static final String APPLICATION_GET_ERR = "User with id %s has not applied for trip with id %s";
    private final SessionFactory sessionFactory;

    @Override
    public Application get(Trip trip, User user) {
        try (Session session = sessionFactory.openSession()) {
            String applicationQuery = "from Application where trip = :trip and user = :user";
            Query<Application> query = session.createQuery(applicationQuery, Application.class);
            query.setParameter("trip", trip);
            query.setParameter("user", user);
            List<Application> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException(String.format(APPLICATION_GET_ERR, user.getId(), trip.getId()));
            }
            return result.get(0);

        }
    }


    public void update(Application application) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(application);
            session.getTransaction().commit();
        }
    }


}
