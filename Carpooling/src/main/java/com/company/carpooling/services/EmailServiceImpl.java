package com.company.carpooling.services;

import com.company.carpooling.repositories.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final EmailRepository emailRepository;

    public EmailServiceImpl(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public void sendEmail(String mailAddress, String title, String mailMessage) {
        emailRepository.sendEmail(mailAddress, title, mailMessage);
    }
}
