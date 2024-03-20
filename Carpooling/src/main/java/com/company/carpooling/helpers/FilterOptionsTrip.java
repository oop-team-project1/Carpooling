package com.company.carpooling.helpers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class FilterOptionsTrip {
    private Optional<String> status;
    private Optional<String> startPointStreet;
    private Optional<String> startPointCity;
    private Optional<String> startPointCountry;
    private Optional<String> endPointStreet;
    private Optional<String> endPointCity;
    private Optional<String> endPointCountry;
    private Optional<String> username;
    private Optional<Integer> passengersCount;
    @JsonFormat(pattern = "HH:mm")
    private Optional<String> departureTime;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Optional<String> departureDate;
    private Optional<String> dateOfCreation;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilterOptionsTrip(String status, String startPointStreet, String startPointCity, String startPointCountry,
                             String endPointStreet, String endPointCity, String endPointCountry,
                             String username, Integer passengersCount, String departureTime, String departureDate,String dateOfCreation,
                             String sortBy, String sortOrder) {
        this.status = Optional.ofNullable(status);
        this.startPointStreet = Optional.ofNullable(startPointStreet);
        this.startPointCity = Optional.ofNullable(startPointCity);
        this.startPointCountry = Optional.ofNullable(startPointCountry);
        this.endPointStreet = Optional.ofNullable(endPointStreet);
        this.endPointCity = Optional.ofNullable(endPointCity);
        this.endPointCountry = Optional.ofNullable(endPointCountry);
        this.username = Optional.ofNullable(username);
        this.passengersCount = Optional.ofNullable(passengersCount);
        this.departureTime = Optional.ofNullable(departureTime);
        this.departureDate =Optional.ofNullable(departureDate);
        this.dateOfCreation = Optional.ofNullable(dateOfCreation);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }


}
