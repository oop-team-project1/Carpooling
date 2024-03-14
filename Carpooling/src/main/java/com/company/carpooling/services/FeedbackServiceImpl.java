package com.company.carpooling.services;

import com.company.carpooling.exceptions.AuthorizationException;
import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.exceptions.TripNotCompletedException;
import com.company.carpooling.exceptions.UserIsNotFromTrip;
import com.company.carpooling.models.*;
import com.company.carpooling.repositories.FeedbackRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    public static final String COMMENT_CREATOR_PERMISSION_ERROR = "You are not creator of the feedback!";

    public static final String BLOCKED_USER_ERROR = "Unable to create feedback, user is blocked";
    public static final String TRIP_NOT_COMPLETED_ERR = "Feedback cannot be created for an incomplete trip. The trip must be completed before providing feedback.";
    public static final String DRIVER_IS_NOT_FROM_TRIP_ERR = "The specified user is not the driver of the given trip.";
    public static final String PASSENGER_IS_NOT_FROM_TRIP_ERR = "The specified user is not an accepted passenger in the given trip.";
    private final FeedbackRepository feedbackRepository;

    @Override
    public List<Feedback> get(int userId) {
        return feedbackRepository.get(userId);
    }

    @Override
    public Feedback getById(int id) {
        return feedbackRepository.getById(id);
    }

    @Override
    public void leaveFeedbackForDriver(User passenger, Feedback feedback, Trip trip, User driver) {
        checkIfBlocked(passenger, BLOCKED_USER_ERROR);
        checkIfTripIsCompleted(trip);
        checkIfDriverIsFromTrip(driver, trip);
        boolean isPassengerFromTrip = checkIfPassengerIsFromTrip(passenger, trip);
        if (!isPassengerFromTrip) {
            throw new UserIsNotFromTrip(PASSENGER_IS_NOT_FROM_TRIP_ERR);
        }
        feedback.setCreator(passenger);
        feedback.setReceiver(driver);
        feedbackRepository.create(feedback);
    }

    @Override
    public void addCommentToFeedback(User fromUser, User toUser, Feedback feedback, FeedbackComment comment) {
        if (fromUser.getId() != feedback.getCreator().getId()) {
            throw new AuthorizationException(COMMENT_CREATOR_PERMISSION_ERROR);
        }

        if (toUser.getId() != feedback.getReceiver().getId()) {
            throw new EntityNotFoundException("User", toUser.getId());
        }

        comment.setFeedback(feedback);
        comment.setUser(fromUser);
        feedbackRepository.createFeedbackComment(comment);
    }

    private void checkIfBlocked(User user, String message) {
        if (user.isBlocked()) {
            throw new AuthorizationException(message);
        }
    }

    private void checkIfTripIsCompleted(Trip trip) {
        if (!trip.getStatus().getStatusName().equals("Completed")) {
            throw new TripNotCompletedException(TRIP_NOT_COMPLETED_ERR);
        }
    }

    private void checkIfDriverIsFromTrip(User user, Trip trip) {
        if (!(trip.getDriver().getId() == user.getId())) {
            throw new UserIsNotFromTrip(DRIVER_IS_NOT_FROM_TRIP_ERR);
        }
    }

    private boolean checkIfPassengerIsFromTrip(User passenger, Trip trip) {
        return trip.getApplications().stream()
                .anyMatch(application ->
                        application.getUser().getId() == passenger.getId()
                                && application.getStatus().getStatus().equals("Approved"));
    }
}
