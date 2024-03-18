package com.company.carpooling.controllers.mvc;

import com.company.carpooling.exceptions.AuthenticationException;
import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.helpers.AuthenticationHelper;
import com.company.carpooling.helpers.FilterOptionsUsers;
import com.company.carpooling.models.User;
import com.company.carpooling.models.dtos.FilterDtoUser;
import com.company.carpooling.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserMvcController {
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

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
}
