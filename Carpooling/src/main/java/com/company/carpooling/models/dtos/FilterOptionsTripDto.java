package com.company.carpooling.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
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
    private String departureTime;
    private String departureDate;
    private String dateOfCreation;
    private String sortBy;
    private String sortOrder;



}
