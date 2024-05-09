package com.company.carpooling.services;

import com.company.carpooling.exceptions.AuthorizationException;
import com.company.carpooling.exceptions.EntityDuplicateException;
import com.company.carpooling.exceptions.FullyBookedException;
import com.company.carpooling.helpers.filters.FilterOptionsTrip;
import com.company.carpooling.models.Application;
import com.company.carpooling.models.PassengerStatus;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.TripStatus;
import com.company.carpooling.models.User;
import com.company.carpooling.repositories.contracts.ApplicationRepository;
import com.company.carpooling.repositories.contracts.TripRepository;
import com.company.carpooling.repositories.contracts.UserRepository;
import com.company.carpooling.services.contracts.TripService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;


@Service
@AllArgsConstructor
public class TripServiceImpl implements TripService {
    public static final String BLOCKED_USER_ERROR = "Unable to modify trip, user is blocked";
    public static final String ENABLED_USER_ERROR = "To proceed, verify your email!";
    public static final String BLOCKED_APPLY_ERR = "Unable to apply for trip, user is blocked";
    public static final String MODIFY_PERMISSION_ERROR = "Only admin or trip creator can modify trip!";
    public static final String INFORMATION_PERMISSION_ERR = "Only trip creator or admin can request passenger info!";
    public static final String DRIVER_CANT_BE_PASSENGER = "As a trip creator you can't apply to be a passenger!";
    public static final String PASSENGER_STATUS_PERMISSION_ERR = "Only trip creators can modify passenger statuses!";
    public static final String PASSENGER_ENABLED_ERR = "The user has not verified his email and can't be added as passenger!";

    TripRepository tripRepository;
    UserRepository userRepository;
    ApplicationRepository applicationRepository;

    @Override
    public List<Trip> get(FilterOptionsTrip filterOptionsTrip) {
        return tripRepository.get(filterOptionsTrip);
    }

    @Override
    public Trip get(int id) {
        return tripRepository.get(id);
    }


    //TODO check time, verified, blocked etc
    @Override
    public void create(Trip trip, User user) {
        checkIfBlocked(user, BLOCKED_USER_ERROR);
        trip.setDriver(user);
        trip.setStatus(new TripStatus(1, "Upcoming"));
        tripRepository.create(trip);
    }

    @Override
    public void update(Trip trip, User user) {
        checkIfBlocked(user, BLOCKED_USER_ERROR);
        checkModifyPermissions(trip, user, MODIFY_PERMISSION_ERROR);
        tripRepository.update(trip);
    }

    @Override
    public void cancelTrip(int id, User user) {
        Trip tripToCancel = tripRepository.get(id);
        checkModifyPermissions(tripToCancel, user, MODIFY_PERMISSION_ERROR);
        tripToCancel.setStatus(new TripStatus(4, "Cancelled"));
        tripRepository.update(tripToCancel);
    }

    @Override
    public Set<Application> getApplications(int tripId, User user) {
        Trip trip = get(tripId);
        checkIfEnabled(user, ENABLED_USER_ERROR);
        checkIfBlocked(user, BLOCKED_APPLY_ERR);
        checkModifyPermissions(trip, user, INFORMATION_PERMISSION_ERR);
        return trip.getApplications();
    }

    @Override
    public void applyForTrip(int tripId, User user) {
        Trip trip = get(tripId);
        checkIfEnabled(user, ENABLED_USER_ERROR);
        checkIfBlocked(user, BLOCKED_APPLY_ERR);
        checkApplicationPermissions(trip, user);
        checkIfAlreadyApplied(trip, user);
        trip.getApplications().add(new Application(trip, user));
        tripRepository.update(trip);
    }

    @Override
    public void cancelParticipation(int tripId, User user) {
        Trip trip = get(tripId);
        trip.getApplications().removeIf(a -> a.getUser().equals(user));
        tripRepository.update(trip);
    }

    @Override
    public void approvePassenger(int tripId, User driver, int applicationId) {
        Trip trip = get(tripId);
        checkIfEnabled(driver, ENABLED_USER_ERROR);
        checkIfBlocked(driver, BLOCKED_USER_ERROR);
        checkStatusModificationPermission(trip, driver);
        if (trip.isFull()) {
            throw new FullyBookedException(trip.getId()
            );
        }
        Application application = applicationRepository.get(applicationId);
        checkIfBlocked(application.getUser(), BLOCKED_APPLY_ERR);
        checkIfEnabled(application.getUser(), PASSENGER_ENABLED_ERR);
        application.setStatus(new PassengerStatus(2, "Approved"));
        applicationRepository.update(application);
    }

    @Override
    public void rejectPassenger(int tripId, User driver, int applicationId) {
        Trip trip = get(tripId);
        checkIfBlocked(driver, BLOCKED_USER_ERROR);
        checkStatusModificationPermission(trip, driver);
        Application application = applicationRepository.get(applicationId);
        application.setStatus(new PassengerStatus(3, "Rejected"));
        applicationRepository.update(application);
    }

    @Override
    public List<Application> getApprovedPassengers(Trip trip) {
        if(trip.getApplications() == null) {
            return Collections.emptyList();
        }
        return trip.getApplications()
                .stream()
                .filter(application -> application.getStatus()
                        .equals(new PassengerStatus(2, "Approved")))
                .toList();
    }

    private void checkIfAlreadyApplied(Trip trip, User user) {
        if (trip.getApplications().stream().anyMatch(application -> application.getUser().equals(user))) {
            throw new EntityDuplicateException("Application from user", "id", user.getId());
        }
    }

    private void checkIfBlocked(User user, String message) {
        if (user.isBlocked()) {
            throw new AuthorizationException(message);
        }
    }

    private void checkIfEnabled(User user, String message) {
        if (user.isBlocked()) {
            throw new AuthorizationException(message);
        }
    }

    private void checkModifyPermissions(Trip trip, User user, String message) {
        if (!(user.isAdmin() || user.getId() == trip.getDriver().getId())) {
            throw new AuthorizationException(message);
        }
    }

    private void checkApplicationPermissions(Trip trip, User user) {
        if (user.getId() == trip.getDriver().getId()) {
            throw new AuthorizationException(DRIVER_CANT_BE_PASSENGER);
        }
    }

    private void checkStatusModificationPermission(Trip trip, User user) {
        if (!(user.getId() == trip.getDriver().getId())) {
            throw new AuthorizationException(PASSENGER_STATUS_PERMISSION_ERR);
        }
    }
}


