package com.company.carpooling.controllers.mvc;

import com.company.carpooling.helpers.FilterOptionsTrip;
import com.company.carpooling.helpers.FilterOptionsUsers;
import com.company.carpooling.services.TripService;
import com.company.carpooling.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final UserService userService;
    private final TripService tripService;

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showHomePage(Model model) {
        model.addAttribute("users", userService.getAll(new FilterOptionsUsers()));
        model.addAttribute("trips", tripService.get(new FilterOptionsTrip()));
        return "HomeView";
    }
}
