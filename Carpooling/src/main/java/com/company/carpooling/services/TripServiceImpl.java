package com.company.carpooling.services;

import com.company.carpooling.exceptions.AuthorizationException;
import com.company.carpooling.helpers.FilterOptionsTrip;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.User;
import com.company.carpooling.repositories.TripRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TripServiceImpl implements TripService {
    public static final String BLOCKED_USER_ERROR = "Unable to modify trip, user is blocked";
    public static final String MODIFY_PERMISSION_ERROR = "Only admin or trip creator can modify trip!";

    TripRepository tripRepository;

    @Override
    public List<Trip> get(FilterOptionsTrip filterOptionsTrip) {
        return tripRepository.get(filterOptionsTrip);
    }

    @Override
    public Trip get(int id) {
        return tripRepository.get(id);
    }

    @Override
    public void create(Trip trip, User user) {
        checkIfBlocked(user);
        trip.setDriver(user);
        tripRepository.create(trip);
    }

    @Override
    public void update(Trip trip, User user) {
        checkIfBlocked(user);
        checkModifyPermissions(trip, user);
        tripRepository.update(trip);
    }

    @Override
    public void delete(int id, User user) {
        Trip tripToDelete = tripRepository.get(id);
        checkModifyPermissions(tripToDelete, user);
        tripToDelete.setStatus(3);
        tripRepository.update(tripToDelete);
    }

    private void checkIfBlocked(User user) {
        if (user.isBlocked()) {
            throw new AuthorizationException(BLOCKED_USER_ERROR);
        }
    }

    private void checkModifyPermissions(Trip trip, User user) {
        if (!(user.isAdmin() || user.getId() == trip.getDriver().getId())) {
            throw new AuthorizationException(MODIFY_PERMISSION_ERROR);
        }
    }

}
