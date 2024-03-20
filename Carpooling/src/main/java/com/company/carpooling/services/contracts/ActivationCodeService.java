package com.company.carpooling.services.contracts;

import com.company.carpooling.models.ActivationCode;
import com.company.carpooling.models.User;

public interface ActivationCodeService {
    ActivationCode getByEmail(String email);
    ActivationCode getByCode(int code);

    void create(ActivationCode activationCode);

    void update(ActivationCode activationCode);

    void deleteActivationCodeByUser(String email);
}
