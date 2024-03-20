package com.company.carpooling.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class FilterOptionsTripDto {
    private String status;
    private String startPointStreet;
    private String startPointCity;
    private String startPointCountry;
    private String endPointStreet;
    private String endPointCity;
    private String endPointCountry;
    private String username;
    private Integer passengersCount;
    private LocalDateTime departureTime;
    private LocalDateTime departureDate;
    private Date dateOfCreation;
    private String sortBy;
    private String sortOrder;



}
