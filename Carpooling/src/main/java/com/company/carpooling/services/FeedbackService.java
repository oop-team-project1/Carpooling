package com.company.carpooling.services;

import com.company.carpooling.models.Feedback;
import com.company.carpooling.models.FeedbackComment;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FeedbackService {

    List<Feedback> get(int userId);

    Feedback getById(int id);
    FeedbackComment getCommentById(int id);

    void leaveFeedbackForDriver(User passenger, Feedback feedback, Trip trip, User driver);

    void leaveFeedbackForPassenger(User driver, Feedback feedback, Trip trip, User passenger);

    void addCommentToFeedback(User fromUser, User toUser, Feedback feedback, FeedbackComment comment);

    void deleteFeedback(User user, User userToDelete, Feedback feedback);
    void deleteFeedbackForComment(User user, User userToDelete, Feedback feedback, FeedbackComment feedbackComment);
}
