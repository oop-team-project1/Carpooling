package com.company.carpooling.repositories;

import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.models.City;
import com.company.carpooling.models.Country;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@AllArgsConstructor
@Repository
public class CityRepositoryImpl implements CityRepository {

    SessionFactory sessionFactory;

    @Override
    public City create(City city,Country country) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            /*Country managedCountry = session.merge(country);
            city.setCountry(managedCountry);*/
            session.persist(city);
            session.getTransaction().commit();
            session.refresh(city);

        }
        return city;
    }

    @Override
    public City getByName(String cityName, Country country) {
        try (Session session = sessionFactory.openSession()) {
            Query<City> query = session.createQuery(
                    "from City where name = :name and country = :country", City.class);
            query.setParameter("name", cityName);
            query.setParameter("country", country);

            List<City> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("City", "name", cityName);
            }

            return result.get(0);
        }

    }

}
