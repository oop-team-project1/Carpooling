package com.company.carpooling.services;

import com.company.carpooling.models.Trip;
import com.company.carpooling.models.User;

public interface TripService {
    Trip get(int id);

    void create(Trip trip);

    void update(Trip trip, User user);
}
