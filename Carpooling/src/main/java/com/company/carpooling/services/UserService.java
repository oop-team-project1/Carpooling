package com.company.carpooling.services;

import com.company.carpooling.models.User;

public interface UserService {
    User getByUsername(String username);
    User getByEmail(String email);
}
