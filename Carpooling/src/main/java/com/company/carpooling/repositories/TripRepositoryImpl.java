package com.company.carpooling.repositories;

import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.helpers.filters.FilterOptionsTrip;
import com.company.carpooling.models.Trip;
import com.company.carpooling.repositories.contracts.TripRepository;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@AllArgsConstructor
@Repository
public class TripRepositoryImpl implements TripRepository {

    private final SessionFactory sessionFactory;


    @Override
    public List<Trip> get(FilterOptionsTrip filterOptions) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        SimpleDateFormat dateParser = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat dateTimeParser = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getStatus().ifPresent(value -> {
                filters.add("status.statusName like :status");
                params.put("status", value);
            });

            filterOptions.getUsername().ifPresent(value -> {
                filters.add("driver.username like :username");
                params.put("username", String.format("%%%s%%", value));
            });

            filterOptions.getStartPointStreet().ifPresent(value -> {
                filters.add("startPoint.streetName like :startPointStreet");
                params.put("startPointStreet", String.format("%%%s%%", value));
            });

            filterOptions.getStartPointCity().ifPresent(value -> {
                if(!value.isBlank()){
                    System.out.println(value);
                filters.add("startPoint.city.name like :startPointCity");
                params.put("startPointCity", String.format("%%%s%%", value));
                }
            });

            filterOptions.getStartPointCountry().ifPresent(value -> {
                filters.add("startPoint.city.country.name like :startPointCountry");
                params.put("startPointCountry", String.format("%%%s%%", value));
            });

            filterOptions.getEndPointStreet().ifPresent(value -> {
                filters.add("endPoint.streetName like :endPointStreet");
                params.put("endPointStreet", String.format("%%%s%%", value));
            });

            filterOptions.getEndPointCity().ifPresent(value -> {
                if(!value.isBlank()) {
                    filters.add("endPoint.city.name like :endPointCity");
                    params.put("endPointCity", String.format("%%%s%%", value));
                }
            });

            filterOptions.getEndPointCountry().ifPresent(value -> {
                filters.add("endPoint.city.country.name like :endPointCountry");
                params.put("endPointCountry", String.format("%%%s%%", value));
            });

            filterOptions.getPassengersCount().ifPresent(value -> {
                filters.add("passengersCount = :passengersCount");
                params.put("passengersCount", value);
            });

            filterOptions.getDepartureDate().ifPresent(value -> {
                if(!value.isBlank()){
                    filters.add("DATE(departureTime) = DATE(:departureDate)");
                    try {
                        params.put("departureDate",dateParser.parse(value));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }

            });

            filterOptions.getDepartureTime().ifPresent(value -> {
                if(!value.isBlank()) {
                    filters.add("TIME(departureTime) = TIME(:departureTime)");
                    params.put("departureTime", (value.isEmpty() ? value : LocalTime.parse(value, timeFormatter)));
                }});
            filterOptions.getDateOfCreation().ifPresent(value -> {
                if(!value.isBlank()) {
                    filters.add("dateOfCreation >= :dateOfCreation");
                    try {
                        params.put("dateOfCreation", (value.isEmpty() ? value : dateTimeParser.parse(value)));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }});

            StringBuilder queryString = new StringBuilder("from Trip");

            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }

            queryString.append(generateOrderBy(filterOptions));

            Query<Trip> query = session.createQuery(queryString.toString(), Trip.class);
            query.setProperties(params);
            return query.list();
        }
    }

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

    private String generateOrderBy(FilterOptionsTrip filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";
        switch (filterOptions.getSortBy().get()) {
            case "startPointCity":
                orderBy = "startPoint.city.name";
                break;
            case "endPointCity":
                orderBy = "endPoint.city.name";
                break;
            case "dateOfCreation":
                orderBy = "dateOfCreation";
                break;
            case "endPointCountry":
                orderBy = "endPoint.city.country.name";
                break;
            case "startPointCountry":
                orderBy = "startPoint.city.country.name";
                break;

        }

        orderBy = String.format(" order by %s", orderBy);

        if (filterOptions.getSortOrder().isPresent() && filterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }
}
