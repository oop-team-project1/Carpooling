package com.company.carpooling.repositories;

import com.company.carpooling.models.Trip;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class TripRepositoryImpl implements TripRepository {

    private final SessionFactory sessionFactory;

    public void create(Trip trip) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(trip);
            session.getTransaction().commit();
        }
    }
}
