package com.company.carpooling.repositories;


import com.company.carpooling.models.ActivationCode;

public interface ActivationCodeRepository {
    ActivationCode getByUsername(String username);
    ActivationCode getByCode(int code);

    void create(ActivationCode activationCode);

    void update(ActivationCode activationCode);

    void deleteActivationCode(int code);
}
