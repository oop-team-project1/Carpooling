package com.company.carpooling.services;

import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.models.Country;
import com.company.carpooling.repositories.contracts.CountryRepository;
import com.company.carpooling.services.contracts.CountryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CountryServiceImpl implements CountryService {

    CountryRepository countryRepository;

    @Override
    public Country fetchOrCreate(Country country) {
        try {
            return countryRepository.getByName(country.getName());
        } catch (EntityNotFoundException e) {
            return countryRepository.create(country);
        }
    }
}
