package com.company.carpooling.controllers.rest;

import com.company.carpooling.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("roadbuddy/api/v1/email")
public class EmailController {
    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public void sendMail() {
        emailService.sendEmail("roadbuddy.carpooling@gmail.com", "Title", "Massage");
    }
}
