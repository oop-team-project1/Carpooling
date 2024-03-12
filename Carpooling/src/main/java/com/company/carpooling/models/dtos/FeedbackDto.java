package com.company.carpooling.models.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedbackDto {

    @NotNull(message = "Rating cannot be null!")
    private int rating;

}
