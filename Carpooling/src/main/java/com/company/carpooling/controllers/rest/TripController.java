package com.company.carpooling.controllers.rest;

import com.company.carpooling.exceptions.AuthenticationException;
import com.company.carpooling.exceptions.AuthorizationException;
import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.exceptions.FullyBookedException;
import com.company.carpooling.helpers.AuthenticationHelper;
import com.company.carpooling.helpers.FilterOptionsTrip;
import com.company.carpooling.helpers.TripMapper;
import com.company.carpooling.models.Application;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.TripDto;
import com.company.carpooling.models.User;
import com.company.carpooling.services.TripService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("roadbuddy/api/v1/trips")
public class TripController {

    private final TripService tripService;
    private final TripMapper tripMapper;
    private final AuthenticationHelper authenticationHelper;

    // TODO filter by status, start point, end point, free spots, driver
    @GetMapping
    public List<Trip> get(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                          @RequestParam(required = false) String status,
                          @RequestParam(required = false) String startPointStreet,
                          @RequestParam(required = false) String startPointCity,
                          @RequestParam(required = false) String startPointCountry,
                          @RequestParam(required = false) String endPointStreet,
                          @RequestParam(required = false) String endPointCity,
                          @RequestParam(required = false) String endPointCountry,
                          @RequestParam(required = false) String username,
                          @RequestParam(required = false) Integer passengersCount,
                          @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime departureTime,
                          @RequestParam(required = false) Date dateOfCreation,
                          @RequestParam(required = false) String sortBy,
                          @RequestParam(required = false) String sortOrder) {

        FilterOptionsTrip filterOptionsTrip = new FilterOptionsTrip(status, startPointStreet,
                startPointCity, startPointCountry, endPointStreet, endPointCity, endPointCountry, username,
                passengersCount, departureTime, dateOfCreation, sortBy, sortOrder);
        try {
            authenticationHelper.tryGetUser(encodedString);
            return tripService.get(filterOptionsTrip);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Trip get(@PathVariable int id,
                    @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            authenticationHelper.tryGetUser(encodedString);
            return tripService.get(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping
    public void create(@RequestBody TripDto tripDto,
                       @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            Trip trip = tripMapper.fromTripDto(tripDto);
            tripService.create(trip, user);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/{id}")
    public Trip update(@PathVariable int id,
                       @RequestBody TripDto tripDto,
                       @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            Trip trip = tripMapper.fromTripDto(id, tripDto);
            tripService.update(trip, user);
            return trip;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id,
                       @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            tripService.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}/applications")
    public Set<Application> getApplications(@PathVariable int id,
                                            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            return tripService.getApplications(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/{id}/applications")
    public void applyForTrip(@PathVariable int id, @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            tripService.applyForTrip(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }

    }

    @DeleteMapping("/{id}/applications")
    public void cancelParticipation(@PathVariable int id, @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            tripService.cancelParticipation(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PostMapping("/{id}/applications/{passengerId}")
    public void approvePassenger(@PathVariable int id, @PathVariable int passengerId, @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            tripService.approvePassenger(id, user, passengerId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (FullyBookedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}/applications/{passengerId}")
    public void rejectPassenger(@PathVariable int id, @PathVariable int passengerId, @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            tripService.rejectPassenger(id, user, passengerId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }


}
