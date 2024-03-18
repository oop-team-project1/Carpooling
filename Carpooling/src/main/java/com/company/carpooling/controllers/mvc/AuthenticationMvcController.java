package com.company.carpooling.controllers.mvc;

import com.company.carpooling.exceptions.AuthorizationException;
import com.company.carpooling.exceptions.EntityDuplicateException;
import com.company.carpooling.exceptions.WrongActivationCodeException;
import com.company.carpooling.helpers.AuthenticationHelper;
import com.company.carpooling.helpers.UserMapper;
import com.company.carpooling.models.User;
import com.company.carpooling.models.dtos.ActivationCodeDto;
import com.company.carpooling.models.dtos.LoginDto;
import com.company.carpooling.models.dtos.RegisterDto;
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
    public static final String PASSWORD_CONFIRMATION_ERROR = "Password confirmation should match password.";
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    @Autowired
    public AuthenticationMvcController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
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

    @GetMapping("/register")
    public String showRegisterPage (Model model) {
        model.addAttribute("register", new RegisterDto());
        return "RegisterView";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDto register,
                                 BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "RegisterView";
        }
        if(!register.getPassword().equals(register.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password_error", PASSWORD_CONFIRMATION_ERROR);
            return "RegisterView";
        }

        try {
            User user = userMapper.fromDtoRegister(register);
            userService.create(user);
            return "redirect:/auth/verify";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            return "RegisterView";
        }
    }

    @GetMapping("/verify")
    public String showEmailConfirmationPage (Model model) {
        model.addAttribute("email", new ActivationCodeDto());
        return "EmailConfirmationView";
    }

    @PostMapping("/verify")
    public String handleEmailConfirmation(@Valid @ModelAttribute("email") ActivationCodeDto activationCodeDto,
                                 BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "EmailConfirmationView";
        }
        try {
            userService.activateAccount(Integer.parseInt(activationCodeDto.getActivationCode()));
            return "redirect:/auth/login";
        } catch (WrongActivationCodeException e) {
            bindingResult.rejectValue("code", "code_error", e.getMessage());
            return "EmailConfirmationView";
        }
    }
}
