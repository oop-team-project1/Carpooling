package com.company.carpooling.controllers.mvc;

import com.company.carpooling.exceptions.AuthenticationException;
import com.company.carpooling.exceptions.AuthorizationException;
import com.company.carpooling.exceptions.EntityDuplicateException;
import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.helpers.AuthenticationHelper;
import com.company.carpooling.helpers.FilterOptionsUsers;
import com.company.carpooling.helpers.UserMapper;
import com.company.carpooling.models.Application;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.User;
import com.company.carpooling.models.dtos.FilterDtoUser;
import com.company.carpooling.models.dtos.UserDto;
import com.company.carpooling.services.UserProfilePicService;
import com.company.carpooling.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserMvcController {
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    private final UserProfilePicService userProfilePicService;


    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showAllUsers(@ModelAttribute("filterOptions") FilterDtoUser filterDto, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }

        FilterOptionsUsers filterOptions = new FilterOptionsUsers(
                filterDto.getUsername(),
                filterDto.getPhoneNumber(),
                filterDto.getEmail(),
                filterDto.getSortBy(),
                filterDto.getSortOrder());

        List<User> users = userService.getAll(filterOptions);
        if (populateIsAuthenticated(session)){
            String currentUsername = (String) session.getAttribute("currentUser");
            model.addAttribute("currentUser", userService.getByEmail(currentUsername));
        }
        model.addAttribute("filterOptionsUsers", filterDto);
        model.addAttribute("users", users);
        return "UsersView";
    }

    @PostMapping("/blocks/{id}")
    public String updateBlocked(@PathVariable int id, Model model, HttpSession session){
        try {
            User user;
            try {
                user = authenticationHelper.tryGetUser(session);
            } catch (AuthenticationException e) {
                return "redirect:/auth/login";
            }

            User userToBlock = userService.getById(id);
            if(userToBlock.isBlocked()){
                userService.unblockUser(userToBlock.getId(), user);
            } else {
                userService.blockUser(userToBlock.getId(), user);
            }
            return "redirect:/users";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/admin/{id}")
    public String makeAdmin(@PathVariable int id, Model model, HttpSession session){
        try {
            User user;
            try {
                user = authenticationHelper.tryGetUser(session);
            } catch (AuthenticationException e) {
                return "redirect:/auth/login";
            }

            User userAdmin = userService.getById(id);
            if(userAdmin.isAdmin()){
                userService.removeAdmin(userAdmin.getId(), user);
            } else {
                userService.makeAdmin(userAdmin.getId(), user);
            }
            return "redirect:/users";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}")
    public String showSingleUser(@PathVariable int id, Model model, HttpSession session) {
        try {
            try {
                authenticationHelper.tryGetUser(session);
            } catch (AuthenticationException e) {
                return "redirect:/auth/login";
            }

            if (populateIsAuthenticated(session)){
                String currentEmail = (String) session.getAttribute("currentUser");
                model.addAttribute("currentUser", userService.getByEmail(currentEmail));
            }
            User user = userService.getById(id);

            List<Trip> tripsAsPassenger = userService.getTripsAsPassenger(user);
            List<Trip> pendingRides = userService.getPendingRides(user);
            Set<Application> applications = user.getApplications();

            model.addAttribute("user", user);
            model.addAttribute("applications", applications);
            model.addAttribute("tripsAsPassenger", tripsAsPassenger);
            model.addAttribute("pendingRides", pendingRides);
            return "UserView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/applications")
    public String showApplications(@PathVariable int id, Model model, HttpSession session) {
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

            User user = userService.getById(id);
            Set<Application> applications = user.getApplications();

            model.addAttribute("user", user);
            model.addAttribute("applications", applications);
            return "UserApplicationsView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/trips")
    public String showCreatedTrips(@PathVariable int id, Model model, HttpSession session) {
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

            User user = userService.getById(id);
            Set<Trip> tripsAsDriver = user.getTripsAsDriver();

            model.addAttribute("user", user);
            model.addAttribute("trips", tripsAsDriver);
            return "UserTripsView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("{id}/update")
    public String showUserEditPage(@PathVariable int id, Model model, HttpSession session) {
            User user;
            try {
                user = authenticationHelper.tryGetUser(session);
            } catch (AuthenticationException e) {
                return "redirect:/auth/login";
            }

            if (populateIsAuthenticated(session)) {
                String currentUsername = (String) session.getAttribute("currentUser");
                model.addAttribute("currentUser", userService.getByEmail(currentUsername));
            }

            try {
                User getUserById = userService.getById(id);
            } catch (EntityNotFoundException e) {
                model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
                model.addAttribute("error", e.getMessage());
                return "ErrorView";
            }

            UserDto userDto = userMapper.userToUserDto(user);
            model.addAttribute("userToUpdate", userDto);
            return "UserEditView";

    }

    @PostMapping("{id}/update")
    public String handleUserEdit(@PathVariable int id,
                                 @Valid @ModelAttribute("userToUpdate") UserDto dto,
                                 BindingResult bindingResult,
                                 Model model,
                                 HttpSession session) {
        if(bindingResult.hasErrors()) {
            return "UserEditView";
        }

        try {
            User user;
            try {
                user = authenticationHelper.tryGetUser(session);
            } catch (AuthenticationException e) {
                return "redirect:/auth/login";
            }

            User userToUpdate = userMapper.fromDtoUpdating(dto, user);
            userService.update(userToUpdate, user);
            session.removeAttribute("currentUser");
            session.setAttribute("currentUser", userToUpdate.getEmail());
            return "redirect:/users/{id}";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            return "UserEditView";
        }  catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable int id, Model model, HttpSession session){
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }

        try {
            userService.deleteUser(id, user);
            return "redirect:/users";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("{id}/set-profile-picture")
    public String setProfilePicture(@PathVariable int id,
                               Model model,
                               @RequestParam("file") MultipartFile file,
                                    HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }

        try {
            String picUrl = userProfilePicService.uploadPictureToCloudinary(file);
            //User userById = userService.getById(id);
            userService.addProfilePicture(id, user, picUrl);
            return "redirect:/users/{id}";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (RuntimeException e) {
            model.addAttribute("message", e.getMessage());
            return "ErrorView";
        }
    }

}
