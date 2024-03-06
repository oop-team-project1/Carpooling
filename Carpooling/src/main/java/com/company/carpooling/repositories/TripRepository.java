package com.company.carpooling.repositories;

import com.company.carpooling.helpers.FilterOptionsTrip;
import com.company.carpooling.models.Trip;

import java.util.List;

public interface TripRepository {
    List<Trip> get(FilterOptionsTrip filterOptions);

    Trip get(int id);

    void create(Trip trip);

    void update(Trip trip);

}

