package com.company.carpooling.services;

import com.company.carpooling.models.ActivationCode;
import com.company.carpooling.repositories.ActivationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivationCodeServiceImpl implements ActivationCodeService{
    private final ActivationCodeRepository activationCodeRepository;

    @Autowired
    public ActivationCodeServiceImpl(ActivationCodeRepository activationCodeRepository) {
        this.activationCodeRepository = activationCodeRepository;
    }


    @Override
    public ActivationCode getByEmail(String email) {
        return activationCodeRepository.getByEmail(email);
    }

    @Override
    public ActivationCode getByCode(int code) {
        return activationCodeRepository.getByCode(code);
    }

    @Override
    public void create(ActivationCode activationCode) {
        activationCodeRepository.create(activationCode);
    }

    @Override
    public void update(ActivationCode activationCode) {
        activationCodeRepository.update(activationCode);
    }

    @Override
    public void deleteActivationCodeByUser(String email) {
        activationCodeRepository.deleteActivationCodeByUser(email);
    }
}
