package com.company.carpooling.controllers.rest;

import com.company.carpooling.exceptions.AuthenticationException;
import com.company.carpooling.helpers.AuthenticationHelper;
import com.company.carpooling.helpers.TripMapper;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.TripDto;
import com.company.carpooling.models.User;
import com.company.carpooling.services.TripService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@RestController
@RequestMapping("roadbuddy/api/v1/trips")
public class TripController {

    private final TripService tripService;
    private final TripMapper tripMapper;
    private final AuthenticationHelper authenticationHelper;

    @PostMapping
    public void create(@RequestBody TripDto tripDto, @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            Trip trip = tripMapper.fromTripDto(tripDto, user);
            tripService.create(trip);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

}
