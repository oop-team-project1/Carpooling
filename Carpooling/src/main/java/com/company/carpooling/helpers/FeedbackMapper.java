package com.company.carpooling.helpers;

import com.company.carpooling.models.Feedback;
import com.company.carpooling.models.FeedbackDto;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {

    public Feedback fromFeedbackDto (FeedbackDto feedbackDto) {
        Feedback feedback = new Feedback();
        feedback.setRating(feedbackDto.getRating());
        return feedback;
    }
}
