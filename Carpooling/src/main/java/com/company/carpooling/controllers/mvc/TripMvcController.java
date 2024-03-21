package com.company.carpooling.controllers.mvc;

import com.company.carpooling.exceptions.AuthenticationException;
import com.company.carpooling.exceptions.AuthorizationException;
import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.helpers.mappers.AuthenticationHelper;
import com.company.carpooling.helpers.filters.FilterOptionsTrip;
import com.company.carpooling.helpers.mappers.AuthenticationHelper;
import com.company.carpooling.helpers.mappers.CommentMapper;
import com.company.carpooling.helpers.mappers.FeedbackMapper;
import com.company.carpooling.helpers.mappers.CommentMapper;
import com.company.carpooling.helpers.mappers.FeedbackMapper;
import com.company.carpooling.helpers.mappers.TripMapper;
import com.company.carpooling.models.*;
import com.company.carpooling.models.dtos.CommentDto;
import com.company.carpooling.models.dtos.FeedbackDto;
import com.company.carpooling.models.dtos.FilterOptionsTripDto;
import com.company.carpooling.models.dtos.TripDtoCoordinates;
import com.company.carpooling.services.BingMapsService;
import com.company.carpooling.services.contracts.FeedbackService;
import com.company.carpooling.services.contracts.TripService;
import com.company.carpooling.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;


import java.text.ParseException;


import java.util.List;
import java.util.Set;


@Setter
@Getter
@Controller
@PropertySource("classpath:application.properties")
@RequestMapping("/trips")
public class TripMvcController {

    private final BingMapsService bingMapsService;
    private final TripService tripService;
    private final FeedbackService feedbackService;
    private final TripMapper tripMapper;
    private final CommentMapper commentMapper;
    private final String bingMapsKey;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final FeedbackMapper feedbackMapper;
    private static final Logger logger = LoggerFactory.getLogger(TripMvcController.class);

    public TripMvcController(BingMapsService bingMapsService, TripService tripService, TripMapper tripMapper,
                             Environment environment, UserService userService,
                             AuthenticationHelper authenticationHelper, FeedbackMapper feedbackMapper,
                             FeedbackService feedbackService, CommentMapper commentMapper) {
        this.bingMapsService = bingMapsService;
        this.tripService = tripService;
        this.tripMapper = tripMapper;
        this.authenticationHelper = authenticationHelper;
        this.feedbackMapper = feedbackMapper;
        this.commentMapper = commentMapper;
        this.feedbackService = feedbackService;
        this.userService = userService;
        bingMapsKey = environment.getProperty("bingmapkey");

    }

    @GetMapping
    public String showAllTrips(Model model, HttpSession session, @ModelAttribute("filterOptions") FilterOptionsTripDto filterDto) throws ParseException {

        FilterOptionsTrip filterOptionsTrip = new FilterOptionsTrip(
                "Upcoming",
                filterDto.getStartPointStreet(),
                filterDto.getStartPointCity(),
                filterDto.getStartPointCountry(),
                filterDto.getEndPointStreet(),
                filterDto.getEndPointCity(),
                filterDto.getEndPointCountry(),
                filterDto.getUsername(),
                filterDto.getPassengersCount(),
                filterDto.getDepartureTime(),
                filterDto.getDepartureDate(),
                filterDto.getDateOfCreation(),
                filterDto.getSortBy(),
                filterDto.getSortOrder()
        );
        List<Trip> trips = tripService.get(filterOptionsTrip);
        /*if (populateIsAuthenticated(session)) {
            String currentEmail = (String) session.getAttribute("currentUser");
            model.addAttribute("currentUser", userService.getByEmail(currentEmail));
        }*/
        /*      model.addAttribute("filterOptions", filterDto);*/
        model.addAttribute("trips", trips);
        return "AllTripsView";

    }

    @GetMapping("/new")
    public String showNewTripPage(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("trip", new TripDtoCoordinates());
            model.addAttribute("user", user);
            model.addAttribute("bingMapsKey", bingMapsKey);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        return "CreateTripView";
    }

    @PostMapping("/new")
    public String createTrip(@Valid @ModelAttribute("trip") TripDtoCoordinates tripDto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "CreateTripView";
        }

        try {
            Trip trip = tripMapper.fromTripDtoCoordinates(tripDto);
            tripService.create(trip, user);
            return "redirect:/trips";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }

    }

    @GetMapping("/{id}")
    public String showSingleTrip(@PathVariable int id, Model model,
                                 HttpSession session) {
        if (populateIsAuthenticated(session)) {
            String currentEmail = (String) session.getAttribute("currentUser");
            model.addAttribute("currentUser", userService.getByEmail(currentEmail));
        } else {
            return "redirect:/auth/login";
        }
        try {
            Trip trip = tripService.get(id);
            List<Application> approvedPassengers = tripService.getApprovedPassengers(trip);
            model.addAttribute("trip", trip);
            model.addAttribute("approvedPassengersList", approvedPassengers);
            return "TripView2";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }


    @GetMapping("/{id}/users/{userId}/feedbacks")
    public String showFeedbackPage(@PathVariable int id,
                                   @PathVariable int userId,
                                   Model model,
                                   HttpSession session) {
        User user;
        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }

        try {
            Trip trip = tripService.get(id);
            User receiver = userService.getById(userId);
            model.addAttribute("receiver", receiver);
            model.addAttribute("trip", trip);
            model.addAttribute("feedback", new FeedbackDto());
            return "FeedbackView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/users/{userId}/feedbacks")
    public String createFeedbackForPassenger(@Valid @ModelAttribute("feedback") FeedbackDto feedbackDto,
                                             BindingResult bindingResult,
                                             @PathVariable int id,
                                             @PathVariable int userId,
                                             Model model,
                                             HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "FeedbackView";
        }

        try {
            Trip trip = tripService.get(id);
            Feedback feedback = feedbackMapper.fromFeedbackDto(feedbackDto, trip);
            User receiver = userService.getById(userId);
            model.addAttribute("receiver", receiver);
            feedbackService.leaveFeedbackForPassenger(user, feedback, trip, receiver);
            model.addAttribute("createdFeedback", feedback);
            return "redirect:/trips/" + id;
        } catch (EntityNotFoundException | AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }


    @PostMapping("/apply")
    public String applyForTrip(@RequestParam("tripId") int tripId, HttpSession session, Model model) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
            tripService.applyForTrip(tripId, user);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            return "redirect:/trips";
        }

        return "redirect:/trips/{tripId}"; // Redirect to the trips page or any other appropriate page
    }
    @PostMapping("/cancelParticipation")
    public String cancelParticipation(@RequestParam("tripId") int tripId, HttpSession session, Model model) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
            tripService.cancelParticipation(tripId,user);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch(EntityNotFoundException e){
            return "redirect:/trips";
        }

        return "redirect:/trips/{tripId}"; // Redirect to the trips page or any other appropriate page

    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/{tripId}/applications")
    public String showApplications(@PathVariable int tripId, Model model, HttpSession session) {
        try {
            try{
                authenticationHelper.tryGetUser(session);
            } catch (AuthenticationException e) {
                return "redirect:/auth/login";
            }

            if (populateIsAuthenticated(session)){
                String currentEmail = (String) session.getAttribute("currentUser");
                model.addAttribute("currentUser", userService.getByEmail(currentEmail));
            }
            Trip trip = tripService.get(tripId);
            User user = authenticationHelper.tryGetUser(session);
            if(!trip.getDriver().equals(user)){
                return "redirect:/trips";
            }
            Set<Application> applications = trip.getApplications();
            model.addAttribute("tripId", tripId);
            model.addAttribute("trip", trip);
            model.addAttribute("applications", applications);
            return "TripApplicationsView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
    @GetMapping("/{tripId}/applications/{appId}")
    public String showApplications(@PathVariable int tripId,@PathVariable int appId, Model model, HttpSession session) {
           try{
            try{
                authenticationHelper.tryGetUser(session);
            } catch (AuthenticationException e) {
                return "redirect:/auth/login";
            }

            if (populateIsAuthenticated(session)){
                String currentEmail = (String) session.getAttribute("currentUser");
                model.addAttribute("currentUser", userService.getByEmail(currentEmail));
            }
            Trip trip = tripService.get(tripId);
            User user = authenticationHelper.tryGetUser(session);
            if(!trip.getDriver().equals(user)){
                return "redirect:/trips";
            }
            Set<Application> applications = trip.getApplications();
            model.addAttribute("tripId", tripId);
            model.addAttribute("trip", trip);
            model.addAttribute("applications", applications);
            return "TripApplicationsView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

}
