package com.company.carpooling.repositories;

import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.models.Trip;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class TripRepositoryImpl implements TripRepository {

    private final SessionFactory sessionFactory;

    @Override
    public Trip get(int id) {
        try (Session session = sessionFactory.openSession()) {
            Trip trip = session.get(Trip.class, id);
            if (trip == null) {
                throw new EntityNotFoundException("Trip", id);
            }
            return trip;
        }
    }

    public void create(Trip trip) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(trip);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Trip trip) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(trip);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Trip tripToDelete = get(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(tripToDelete);
            session.getTransaction().commit();
        }
    }
}
