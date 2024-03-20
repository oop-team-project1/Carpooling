package com.company.carpooling.repositories.contracts;

import com.company.carpooling.helpers.filters.FilterOptionsUsers;
import com.company.carpooling.models.User;

import java.util.List;

public interface UserRepository {
    List<User> getAll(FilterOptionsUsers filterOptionsUsers);

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByPhoneNumber(String phoneNumber);

    void create(User user);

    void update(User user);

    void deleteUser(int id);
}
