package com.company.carpooling.controllers.rest;

import com.company.carpooling.exceptions.*;
import com.company.carpooling.helpers.AuthenticationHelper;
import com.company.carpooling.helpers.FilterOptionsUsers;
import com.company.carpooling.helpers.UserMapper;
import com.company.carpooling.models.User;
import com.company.carpooling.models.UserDto;
import com.company.carpooling.services.UserProfilePicService;
import com.company.carpooling.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("roadbuddy/api/v1/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final UserProfilePicService userProfilePicService;


    public UserController(UserService userService,
                          AuthenticationHelper authenticationHelper,
                          UserMapper userMapper,
                          UserProfilePicService userProfilePicService) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.userProfilePicService = userProfilePicService;
    }

    @GetMapping
    public List<User> getAll(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                             @RequestParam(required = false) String username,
                             @RequestParam(required = false) String phoneNumber,
                             @RequestParam(required = false) String email,
                             @RequestParam(required = false) String sortBy,
                             @RequestParam(required = false) String sortOrder)
    {
        FilterOptionsUsers filterOptionsUsers = new FilterOptionsUsers(username, phoneNumber,
                email, sortBy, sortOrder);
        try {
            authenticationHelper.tryGetUser(encodedString);
            return userService.getAll();
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

    @PostMapping("/{id}/feedbacks")
    public void leaveFeedback(@PathVariable int id) {
    }

    @GetMapping("/{id}/feedbacks")
    public void getFeedbacks(@PathVariable int id) {
    }

}
