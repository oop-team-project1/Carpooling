package com.company.carpooling.services;

import com.company.carpooling.models.Trip;
import com.company.carpooling.repositories.TripRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TripServiceImpl implements TripService {

    TripRepository tripRepository;

    @Override
    public void create(Trip trip) {
        tripRepository.create(trip);
    }


}
