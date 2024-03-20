package com.company.carpooling.helpers;

import com.company.carpooling.models.*;
import com.company.carpooling.models.dtos.TripDto;
import com.company.carpooling.models.dtos.TripDtoCoordinates;
import com.company.carpooling.models.json.Point;
import com.company.carpooling.repositories.TripRepository;
import com.company.carpooling.services.BingMapsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TripMapper {
    private final BingMapsService bingMapService;
    private final AddressMapper addressMapper;
    private final TripRepository tripRepository;
    private final PointMapper pointMapper;

    public Trip fromTripDto(TripDto dto) {
        Point startingPoint = bingMapService.getCoordinates(dto.getStartPoint());
        Point endingPoint = bingMapService.getCoordinates(dto.getEndPoint());

        Street startPoint = addressMapper.getAddress(startingPoint);
        Street endPoint = addressMapper.getAddress(endingPoint);
        Trip trip = new Trip();
        bingMapService.setDistanceAndDuration(dto, trip);
        //BingServiceLogic for distance
        trip.setDepartureTime(dto.getDepartureTime());
        trip.setStartPoint(startPoint);
        trip.setEndPoint(endPoint);
        trip.setSeatsAvailable(dto.getFreeSpots());
        return trip;
    }

    public Trip fromTripDtoCoordinates(TripDtoCoordinates dto) {
        Point startingPoint = pointMapper.fromDto(dto.getStartPoint());
        Point endingPoint = pointMapper.fromDto(dto.getEndPoint());

        Street startPoint = addressMapper.getAddress(startingPoint);
        Street endPoint = addressMapper.getAddress(endingPoint);
        Trip trip = new Trip();
        bingMapService.setDistanceAndDuration(startingPoint,endingPoint, trip);
        //BingServiceLogic for distance
        trip.setDepartureTime(dto.getDepartureTime());
        trip.setStartPoint(startPoint);
        trip.setEndPoint(endPoint);
        trip.setSeatsAvailable(dto.getFreeSpots());
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
