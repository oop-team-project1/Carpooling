package com.company.carpooling.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TripDto {
    @NotNull
    AddressDto startPoint;

    @NotNull
    AddressDto endPoint;

    @NotNull
    @Future
    @JsonFormat(pattern = "dd/MM/yy HH:mm")
    LocalDateTime departureTime;
    @Range(min = 1, max = 4)
    int freeSpots;


}
