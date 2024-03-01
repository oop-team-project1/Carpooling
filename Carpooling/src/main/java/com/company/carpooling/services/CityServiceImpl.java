package com.company.carpooling.services;

import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.models.City;
import com.company.carpooling.models.Country;
import com.company.carpooling.repositories.CityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CityServiceImpl implements CityService {

    CityRepository cityRepository;


    @Override
    public City fetchOrCreate(City city, Country country) {
        try {
            return cityRepository.getByName(city.getName(), country);

        } catch (EntityNotFoundException e) {
            return cityRepository.create(city, country);
        }
    }
}
