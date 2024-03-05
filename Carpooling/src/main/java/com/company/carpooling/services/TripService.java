package com.company.carpooling.services;

import com.company.carpooling.helpers.FilterOptionsTrip;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.User;

import java.util.List;

public interface TripService {
    List<Trip> get(FilterOptionsTrip filterOptionsTrip);
    Trip get(int id);

    void create(Trip trip, User user);

    void update(Trip trip, User user);

    void cancelTrip (int id, User user);
}
