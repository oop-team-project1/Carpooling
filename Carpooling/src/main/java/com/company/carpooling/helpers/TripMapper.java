package com.company.carpooling.helpers;

import com.company.carpooling.models.*;
import com.company.carpooling.repositories.TripRepository;
import com.company.carpooling.services.BingMapsService;
import com.company.carpooling.services.TripService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TripMapper {
    private final BingMapsService bingMapService;
    private final AddressMapper addressMapper;
    private final TripRepository tripRepository;

    public Trip fromTripDto(TripDto dto) {
        Street startPoint = addressMapper.getAddress(dto.getStartPoint());
        Street endPoint = addressMapper.getAddress(dto.getEndPoint());
        Trip trip = new Trip();
        bingMapService.setDistanceAndDuration(dto, trip);
        //BingServiceLogic for distance
        trip.setDepartureTime(dto.getDepartureTime());
        trip.setStartPoint(startPoint);
        trip.setEndPoint(endPoint);
        trip.setPassengersCount(dto.getFreeSpots());
        /* trip.setStatus(TripStatus.UPCOMING);*/
        return trip;
    }

    public Trip fromTripDto(int id, TripDto tripDto) {
        Trip trip = fromTripDto(tripDto);
        trip.setId(id);
        Trip repositoryTrip = tripRepository.get(id);
        trip.setDriver(repositoryTrip.getDriver());
        return trip;
    }

}
