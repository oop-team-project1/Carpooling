package com.company.carpooling.services;

import com.company.carpooling.models.User;
import com.company.carpooling.services.contracts.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    private String userCreationTemplate(int code, User user) {
        return String.format("Hello %s , \n welcome to Road buddy! \n To activate your account use this code\n %d",
                user.getUsername(), code);
    }

    @Async
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("roadbuddy.carpooling@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public void sendUserCreationVerificationCode(User user, int code) {
        String msg = userCreationTemplate(code, user);
        sendEmail(user.getEmail(), "Account created", msg);
    }
}
