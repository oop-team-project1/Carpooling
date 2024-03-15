package com.company.carpooling.controllers.mvc;

import com.company.carpooling.exceptions.AuthorizationException;
import com.company.carpooling.helpers.AuthenticationHelper;
import com.company.carpooling.models.User;
import com.company.carpooling.models.dtos.LoginDto;
import com.company.carpooling.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public AuthenticationMvcController(UserService userService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "LoginView";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto loginDto,
                              BindingResult bindingResult,
                              HttpSession session) {

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                System.out.println("Message: " + error.getDefaultMessage());
            }
            return "LoginView";
        }

        try {
            User user = authenticationHelper.verifyAuthentication(loginDto.getEmail(), loginDto.getPassword());
            session.setAttribute("currentUser", loginDto.getEmail());
            session.setAttribute("isAdmin", user.isAdmin());
            return "LoginView";

        } catch (AuthorizationException e) {
            bindingResult.rejectValue("email", "auth_error", e.getMessage());
            return "LoginView";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/auth/login";
    }
}
