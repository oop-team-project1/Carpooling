package com.company.carpooling.helpers;

import com.company.carpooling.models.Feedback;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.dtos.FeedbackDto;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {

    public static final String INVALID_FEEDBACK_ERR = "Feedback must be a number between 1 and 5.";

    public Feedback fromFeedbackDto (FeedbackDto feedbackDto, Trip trip) {
        Feedback feedback = new Feedback();
        checkIfFeedbackIsInValidRange(feedbackDto);
        feedback.setRating(feedbackDto.getRating());
        feedback.setTrip(trip);
        return feedback;
    }

    private void checkIfFeedbackIsInValidRange(FeedbackDto feedbackDto) {
        if (feedbackDto.getRating() < 1 || feedbackDto.getRating() > 5) {
            throw new IllegalArgumentException(INVALID_FEEDBACK_ERR);
        }
    }
}
