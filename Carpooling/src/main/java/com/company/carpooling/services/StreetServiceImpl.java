package com.company.carpooling.services;

import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.models.City;
import com.company.carpooling.models.Street;
import com.company.carpooling.repositories.StreetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class StreetServiceImpl implements StreetService {
    StreetRepository streetRepository;

    @Override
    public Street fetchOrCreate(Street street, City city) {
        try {
            return streetRepository.getByName(street.getStreetName(), city);

        } catch (EntityNotFoundException e) {
            return streetRepository.create(street, city);
        }
    }
}
