package com.company.carpooling.services;

import com.company.carpooling.exceptions.AuthorizationException;
import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.models.Feedback;
import com.company.carpooling.models.FeedbackComment;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.User;
import com.company.carpooling.repositories.FeedbackRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    public static final String COMMENT_CREATOR_PERMISSION_ERROR = "You are not creator of the feedback!";

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
    public void create(User fromUser, Feedback feedback, Trip trip, User toUser) {
        //check if blocked
        //check if trip is completed
        //check if both users are from same trip
        feedback.setCreator(fromUser);
        feedback.setReceiver(toUser);
        feedbackRepository.create(feedback);
    }

    @Override
    public void addCommentToFeedback(User fromUser, User toUser, Feedback feedback, FeedbackComment comment) {
        if(fromUser.getId() != feedback.getCreator().getId()) {
            throw new AuthorizationException(COMMENT_CREATOR_PERMISSION_ERROR);
        }

        if(toUser.getId() != feedback.getReceiver().getId()) {
            throw new EntityNotFoundException("User", toUser.getId());
        }

        comment.setFeedback(feedback);
        comment.setUser(fromUser);
        feedbackRepository.createFeedbackComment(comment);
    }
}
