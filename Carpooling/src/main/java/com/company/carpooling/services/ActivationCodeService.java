package com.company.carpooling.services;

import com.company.carpooling.models.ActivationCode;
import com.company.carpooling.models.User;

public interface ActivationCodeService {
    ActivationCode getByUsername(String username);
    ActivationCode getByCode(int code);

    void create(ActivationCode activationCode);

    void update(ActivationCode activationCode);

    void deleteActivationCode(int code);
}
