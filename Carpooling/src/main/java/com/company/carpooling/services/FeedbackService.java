package com.company.carpooling.services;

import com.company.carpooling.models.Feedback;
import com.company.carpooling.models.FeedbackComment;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FeedbackService {

    List<Feedback> get (int userId);
    Feedback getById(int id);
    void create (User fromUser, Feedback feedback, Trip trip, User toUser);
    void addCommentToFeedback(User fromUser, User toUser, Feedback feedback, FeedbackComment comment);
}