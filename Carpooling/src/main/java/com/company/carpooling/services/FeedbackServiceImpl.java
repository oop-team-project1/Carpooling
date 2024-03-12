package com.company.carpooling.services;

import com.company.carpooling.models.Feedback;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.User;
import com.company.carpooling.repositories.FeedbackRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Override
    public List<Feedback> get(int userId) {
        return feedbackRepository.get(userId);
    }

    @Override
    public void create(User fromUser, Feedback feedback, Trip trip, User toUser) {
        //check if blocked
        //check if trip is completed
        //check if both users are from same trip
        feedback.setCreator(fromUser);
        feedback.setReceiver(toUser);
        feedbackRepository.create(feedback);
    }
}
