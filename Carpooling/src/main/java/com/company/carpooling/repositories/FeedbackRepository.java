package com.company.carpooling.repositories;

import com.company.carpooling.models.Feedback;
import com.company.carpooling.models.FeedbackComment;

import java.util.List;

public interface FeedbackRepository {
    List<Feedback> get(int userId);

    Feedback getById(int id);

    void create(Feedback feedback);

    void createFeedbackComment(FeedbackComment comment);

    void delete(Feedback feedback);
}
