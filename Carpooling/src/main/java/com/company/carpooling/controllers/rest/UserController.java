package com.company.carpooling.controllers.rest;

import com.company.carpooling.exceptions.*;
import com.company.carpooling.helpers.*;
import com.company.carpooling.models.*;
import com.company.carpooling.models.dtos.CommentDto;
import com.company.carpooling.models.dtos.FeedbackDto;
import com.company.carpooling.models.dtos.UserDto;
import com.company.carpooling.services.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("roadbuddy/api/v1/users")
public class UserController {
    private final UserService userService;
    private final TripService tripService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final UserProfilePicService userProfilePicService;
    private final FeedbackService feedbackService;
    private final FeedbackMapper feedbackMapper;
    private final CommentMapper commentMapper;


    @GetMapping
    public List<User> getAll(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                             @RequestParam(required = false) String username,
                             @RequestParam(required = false) String phoneNumber,
                             @RequestParam(required = false) String email,
                             @RequestParam(required = false) String sortBy,
                             @RequestParam(required = false) String sortOrder) {
        FilterOptionsUsers filterOptionsUsers = new FilterOptionsUsers(username, phoneNumber,
                email, sortBy, sortOrder);
        try {
            authenticationHelper.tryGetUser(encodedString);
            return userService.getAll(filterOptionsUsers);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id,
                    @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            authenticationHelper.tryGetUser(encodedString);
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/username")
    public User getByUsername(@RequestParam String username,
                              @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            authenticationHelper.tryGetUser(encodedString);
            return userService.getByUsername(username);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @GetMapping("/email")
    public User getByEmail(@RequestParam String email,
                           @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            authenticationHelper.tryGetUser(encodedString);
            return userService.getByEmail(email);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @GetMapping("/phoneNumber")
    public User getByPhoneNumber(@RequestParam String phoneNumber,
                                 @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            authenticationHelper.tryGetUser(encodedString);
            return userService.getByPhoneNumber(phoneNumber);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PostMapping
    public User create(@Valid @RequestBody UserDto userDto) {
        try {
            User userToCreate = userMapper.fromDto(userDto);
            userService.create(userToCreate);
            return userToCreate;
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @PutMapping("/{id}/avatar")
    public String setProfilePicture(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                                    @PathVariable int id,
                                    @RequestParam("avatar") MultipartFile file) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            String avatar = userProfilePicService.uploadPictureToCloudinary(file);
            userService.addProfilePicture(id, user, avatar);
            return avatar;
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public User update(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                       @PathVariable int id,
                       @Valid @RequestBody UserDto userDto) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            User userToUpdate = userMapper.fromDto(id, userDto);
            userService.update(userToUpdate, user);
            return userToUpdate;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                       @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            userService.deleteUser(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/blocks/{id}")
    public void blockUser(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                          @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            userService.blockUser(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (BlockedUnblockedUserException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @DeleteMapping("/blocks/{id}")
    public void unblockUser(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                            @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            userService.unblockUser(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (BlockedUnblockedUserException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PutMapping("/admin/{id}")
    public void makeAdmin(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                          @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            userService.makeAdmin(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (AdminException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @DeleteMapping("/admin/{id}")
    public void removeAdmin(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                            @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            userService.removeAdmin(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (AdminException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }


    //TODO filter and sort trips
    @GetMapping("/{id}/trips")
    public void getUserTrips(@PathVariable int id) {
    }

    @PostMapping("/{id}/trips/{tripId}/feedback-driver")
    public void leaveFeedbackForDriver(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                                       @PathVariable int id,
                                       @PathVariable int tripId,
                                       @Valid @RequestBody FeedbackDto feedbackDto) {
        try {
            User passenger = authenticationHelper.tryGetUser(encodedString);
            User driver = userService.getById(id);
            Trip trip = tripService.get(tripId);
            Feedback feedback = feedbackMapper.fromFeedbackDto(feedbackDto, trip);
            feedbackService.leaveFeedbackForDriver(passenger, feedback, trip, driver);
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (TripNotCompletedException | UserIsNotFromTrip | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{id}/trips/{tripId}/feedback-passenger")
    public void leaveFeedbackForPassenger(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                                          @PathVariable int id,
                                          @PathVariable int tripId,
                                          @Valid @RequestBody FeedbackDto feedbackDto) {
        try {
            User driver = authenticationHelper.tryGetUser(encodedString);
            User passenger = userService.getById(id);
            Trip trip = tripService.get(tripId);
            Feedback feedback = feedbackMapper.fromFeedbackDto(feedbackDto, trip);
            feedbackService.leaveFeedbackForPassenger(driver, feedback, trip, passenger);
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (TripNotCompletedException | UserIsNotFromTrip | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{id}/feedbacks/{feedbackId}/comments")
    public void addCommentToFeedback(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                                     @PathVariable int id,
                                     @PathVariable int feedbackId,
                                     @Valid @RequestBody CommentDto commentDto) {

        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            Feedback feedback = feedbackService.getById(feedbackId);
            FeedbackComment comment = commentMapper.fromCommentDto(commentDto);
            User toUser = userService.getById(id);
            feedbackService.addCommentToFeedback(user, toUser, feedback, comment);
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}/feedbacks")
    public List<Feedback> getFeedbacks(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                                       @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(encodedString);
            return feedbackService.get(id);
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}/feedbacks/{feedbackId}")
    public void deleteFeedbackForUser(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                                      @PathVariable int id,
                                      @PathVariable int feedbackId) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            User userToDeleteFeedback = userService.getById(id);
            Feedback feedbackToDelete = feedbackService.getById(feedbackId);
            feedbackService.deleteFeedback(user, userToDeleteFeedback, feedbackToDelete);
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}/feedbacks/{feedbackId}/comments/{commentId}")
    public void deleteCommentForFeedback(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                                         @PathVariable int id,
                                         @PathVariable int feedbackId,
                                         @PathVariable int commentId) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            User userToDeleteFeedback = userService.getById(id);
            Feedback feedback = feedbackService.getById(feedbackId);
            FeedbackComment comment = feedbackService.getCommentById(commentId);
            feedbackService.deleteFeedbackForComment(user, userToDeleteFeedback, feedback, comment);
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/activation/{code}")
    public String activateUser(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                               @PathVariable int code) {
        try {
            authenticationHelper.tryGetUser(encodedString);
            userService.activateAccount(code);
            return "Account activated";
        } catch (WrongActivationCodeException e) {
            return "Code has expired! Resend code.";
        } catch (ForbiddenOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PostMapping("/activation/new-code")
    public String sendNewActivationCode(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            userService.resendActivationCode(user.getUsername());
            return "New activation code sent";
        } catch (ForbiddenOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }
}
