package com.company.carpooling.services;

import com.company.carpooling.models.User;

public interface EmailService {
    void sendEmail(String to, String subject, String text);

    void sendUserCreationVerificationCode(User user, int code) ;
}
