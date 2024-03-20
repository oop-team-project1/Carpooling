package com.company.carpooling.repositories;

import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.helpers.FilterOptionsUsers;
import com.company.carpooling.models.User;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository{

    private final SessionFactory sessionFactory;

    @Override
    public List<User> getAll(FilterOptionsUsers filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getUsername().ifPresent(value -> {
                filters.add("username like :username");
                params.put("username", String.format("%%%s%%", value));
            });

            filterOptions.getPhoneNumber().ifPresent(value -> {
                filters.add("phoneNumber like :phoneNumber");
                params.put("phoneNumber", String.format("%%%s%%", value));
            });

            filterOptions.getEmail().ifPresent(value -> {
                filters.add("email like :email");
                params.put("email", String.format("%%%s%%", value));
            });

            StringBuilder queryString = new StringBuilder("from User");
            if (!filters.isEmpty()) {
                queryString.append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(filterOptions));

            Query<User> query = session.createQuery(queryString.toString(), User.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public User getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            return user;
        }
    }

    @Override
    public User getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(
                    "from User where username = :username", User.class);
            query.setParameter("username", username);

            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "username", username);
            }

            return result.get(0);
        }
    }

    @Override
    public User getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(
                    "from User where email = :email", User.class);
            query.setParameter("email", email);

            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "email", email);
            }

            return result.get(0);
        }
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(
                    "from User where phoneNumber = :phoneNumber", User.class);
            query.setParameter("phoneNumber", phoneNumber);

            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "phone number", phoneNumber);
            }

            return result.get(0);
        }
    }

    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteUser(int id) {
        User userToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(userToDelete);
            session.getTransaction().commit();
        }
    }

    private String generateOrderBy(FilterOptionsUsers filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";
        switch (filterOptions.getSortBy().get()) {
            case "phoneNumber":
                orderBy = "phoneNumber";
                break;
            case "username":
                orderBy = "username";
                break;
            case "email":
                orderBy = "email";
                break;
        }

        if (!orderBy.isEmpty()) {
            orderBy = " ORDER BY " + orderBy;

            // Append sorting order if specified
            if (filterOptions.getSortOrder().isPresent() &&
                    filterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
                orderBy += " DESC";
            }
        }

        return orderBy;
    }
}
