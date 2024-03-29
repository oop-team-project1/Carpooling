package com.company.carpooling.helpers.mappers;

import com.company.carpooling.models.FeedbackComment;
import com.company.carpooling.models.dtos.CommentDto;
import com.company.carpooling.services.contracts.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommentMapper {
    private final FeedbackService feedbackService;

    public FeedbackComment fromCommentDto (CommentDto commentDto) {
        FeedbackComment comment = new FeedbackComment();
        comment.setContent(commentDto.getContent());
        return comment;
    }
}
