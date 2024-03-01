package com.company.carpooling.repositories;

import com.company.carpooling.exceptions.EntityNotFoundException;

import com.company.carpooling.models.Country;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@AllArgsConstructor
@Repository
public class CountryRepositoryImpl implements CountryRepository {

    SessionFactory sessionFactory;
    @Override
    public Country getByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Country> query = session.createQuery(
                    "from Country where name = :name", Country.class);
            query.setParameter("name", name);

            List<Country> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Country", "name", name);
            }

            return result.get(0);
        }

    }

    public Country create(Country country) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(country);
            session.getTransaction().commit();
            session.refresh(country);
        }
        return country;
    }


}
