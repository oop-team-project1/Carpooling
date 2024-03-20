package com.company.carpooling.repositories;


import com.company.carpooling.models.ActivationCode;

public interface ActivationCodeRepository {
    ActivationCode getByEmail(String email);
    ActivationCode getByCode(int code);

    void create(ActivationCode activationCode);

    void update(ActivationCode activationCode);

    void deleteActivationCodeByUser(String email);
}
