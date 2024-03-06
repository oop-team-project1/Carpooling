package com.company.carpooling.services;

import com.company.carpooling.helpers.FilterOptionsTrip;
import com.company.carpooling.models.Application;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.User;

import java.util.List;
import java.util.Set;

public interface TripService {
    List<Trip> get(FilterOptionsTrip filterOptionsTrip);

    Trip get(int id);

    void create(Trip trip, User user);

    void update(Trip trip, User user);

    void delete(int id, User user);

    Set<Application> getApplications(int tripId, User user);

    void applyForTrip(int tripId, User user);

    void cancelParticipation(int tripId, User user);

    void approvePassenger(int tripId, User driver, int passengerId);

    void rejectPassenger(int tripId, User driver, int passengerId);
}
