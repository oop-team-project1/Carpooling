package com.company.carpooling.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Repository;

@Repository
public class EmailRepositoryImpl implements EmailRepository{
    private final Environment env;

    private final JavaMailSender mailSender;

    @Autowired
    public EmailRepositoryImpl(Environment env, JavaMailSender mailSender) {
        this.env = env;
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String mailAddress, String title, String mailMessage) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(env.getProperty("spring.mail.username"));
        message.setTo(mailAddress);
        message.setSubject(title);
        message.setText(mailMessage);

        mailSender.send(message);
    }
}
