package com.company.carpooling.repositories;

import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.models.City;
import com.company.carpooling.models.Street;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@AllArgsConstructor
@Repository
public class StreetRepositoryImpl implements StreetRepository {

    SessionFactory sessionFactory;

    @Override
    public Street create(Street street,City city) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            /*City managedCity = session.merge(city);
            street.setCity(managedCity);*/
            session.persist(street);
            session.getTransaction().commit();
            session.refresh(street);
        }
        return street;
    }

    @Override
    public Street getByName(String streetName, City city) {
        try (Session session = sessionFactory.openSession()) {
            Query<Street> query = session.createQuery(
                    "from Street where streetName = :name and city = :city", Street.class);
            query.setParameter("name", streetName);
            query.setParameter("city", city);

            List<Street> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Street", "name", streetName);
            }

            return result.get(0);
        }

    }

}
