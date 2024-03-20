package com.company.carpooling.controllers.rest;

import com.company.carpooling.exceptions.*;
import com.company.carpooling.helpers.mappers.AuthenticationHelper;
import com.company.carpooling.helpers.filters.FilterOptionsTrip;
import com.company.carpooling.helpers.mappers.TripMapper;
import com.company.carpooling.models.Application;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.dtos.TripDto;
import com.company.carpooling.models.User;
import com.company.carpooling.services.contracts.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    @Operation(security = {@SecurityRequirement(name = "basic")})
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
                          @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") String departureTime,
                          @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") String departureDate,
                          @RequestParam(required = false) String dateOfCreation,
                          @RequestParam(required = false) String sortBy,
                          @RequestParam(required = false) String sortOrder) {

        FilterOptionsTrip filterOptionsTrip = new FilterOptionsTrip(status, startPointStreet,
                startPointCity, startPointCountry, endPointStreet, endPointCity, endPointCountry, username,
                passengersCount, departureTime,departureDate, dateOfCreation, sortBy, sortOrder);
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
    @Operation(security = {@SecurityRequirement(name = "basic")})
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
    @Operation(security = {@SecurityRequirement(name = "basic")})
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
    @Operation(security = {@SecurityRequirement(name = "basic")})
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
    @Operation(security = {@SecurityRequirement(name = "basic")})
    public void cancelTrip(@PathVariable int id,
                           @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            tripService.cancelTrip(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}/applications")
    @Operation(security = {@SecurityRequirement(name = "basic")})
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
    @Operation(security = {@SecurityRequirement(name = "basic")})
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
        } catch (EntityDuplicateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

    }

    @DeleteMapping("/{id}/applications")
    @Operation(security = {@SecurityRequirement(name = "basic")})
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

    @PostMapping("/{id}/applications/{applicationId}")
    @Operation(security = {@SecurityRequirement(name = "basic")})
    public void approvePassenger(@PathVariable int id, @PathVariable int applicationId, @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            tripService.approvePassenger(id, user, applicationId);
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

    @DeleteMapping("/{id}/applications/{applicationId}")
    @Operation(security = {@SecurityRequirement(name = "basic")})
    public void rejectPassenger(@PathVariable int id, @PathVariable int applicationId, @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            tripService.rejectPassenger(id, user, applicationId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }


}
