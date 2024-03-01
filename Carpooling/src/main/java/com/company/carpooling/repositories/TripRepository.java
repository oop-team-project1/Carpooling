package com.company.carpooling.repositories;

import com.company.carpooling.models.Trip;

public interface TripRepository {
    Trip get(int id);

    void create(Trip trip);

    void update(Trip trip);

    void delete(int id);

}

