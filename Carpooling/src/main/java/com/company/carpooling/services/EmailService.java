package com.company.carpooling.services;

public interface EmailService {
    void sendEmail(String mailAddress, String title, String mailMessage);
}
