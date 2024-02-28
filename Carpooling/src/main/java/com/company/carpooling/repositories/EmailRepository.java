package com.company.carpooling.repositories;

public interface EmailRepository {
    void sendEmail(String mailAddress, String title, String mailMessage);
}
