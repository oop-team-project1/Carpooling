package com.company.carpooling.services;

import com.company.carpooling.exceptions.AuthorizationException;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.User;
import com.company.carpooling.repositories.TripRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TripServiceImpl implements TripService {
    public static final String BLOCKED_USER_ERROR = "Unable to modify trip, user is blocked";
    public static final String MODIFY_PERMISSION_ERROR = "Only admin or trip creator can modify trip!";

    TripRepository tripRepository;

    @Override
    public Trip get(int id) {
        return tripRepository.get(id);
    }

    @Override
    public void create(Trip trip) {
        tripRepository.create(trip);
    }

    @Override
    public void update(Trip trip, User user) {
        checkIfBlocked(user);
        checkModifyPermissions(trip, user);
        tripRepository.update(trip);
    }

    private void checkIfBlocked(User user) {
        if (user.isBlocked()) {
            throw new AuthorizationException(BLOCKED_USER_ERROR);
        }
    }

    private void checkModifyPermissions(Trip trip, User user) {
        if (!(user.isAdmin() && user.getId() != trip.getDriver().getId())) {
            throw new AuthorizationException(MODIFY_PERMISSION_ERROR);
        }
    }


}
