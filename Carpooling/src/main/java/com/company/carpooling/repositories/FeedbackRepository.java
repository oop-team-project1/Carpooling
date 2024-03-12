package com.company.carpooling.repositories;

import com.company.carpooling.models.Feedback;

import java.util.List;

public interface FeedbackRepository {
    List<Feedback> get (int userId);
    void create (Feedback feedback);
}
