package com.company.carpooling.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripDtoCoordinates {
    @NotNull
    @Valid
    PointDto startPoint;
    @NotNull
    @Valid
    PointDto endPoint;

    @NotNull
    @Future
    @JsonFormat(pattern = "dd/MM/yy HH:mm")
    LocalDateTime departureTime;
    @Range(min = 1, max = 4)
    int freeSpots;
}
