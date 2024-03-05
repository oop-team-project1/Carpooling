package com.company.carpooling.services;

import com.company.carpooling.exceptions.AuthorizationException;
import com.company.carpooling.helpers.FilterOptionsTrip;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.User;
import com.company.carpooling.repositories.TripRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.company.carpooling.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class TripServiceTests {
    @Mock
    TripRepository mockRepository;

    @InjectMocks
    TripServiceImpl tripService;

    @Test
    public void get_Should_CallRepository() {
        FilterOptionsTrip mockFilterOptions = createMockFilterOptionsForTrip();
        Mockito.when(mockRepository.get(mockFilterOptions)).thenReturn(null);

        tripService.get(mockFilterOptions);

        Mockito.verify(mockRepository, Mockito.times(1)).get(mockFilterOptions);
    }

    @Test
    public void get_Should_ReturnTrip_When_MatchByIdExist() {
        Trip mockTrip = createMockTrip();

        Mockito.when(mockRepository.get(Mockito.anyInt())).thenReturn(mockTrip);
        Trip result = tripService.get(mockTrip.getId());

        Assertions.assertEquals(mockTrip,result);
    }

    @Test
    public void create_Should_CallRepository_When_UserIsNotBlocked () {
            Trip trip = createMockTrip();
            User user = createMockUser();

            Mockito.doNothing().when(mockRepository).create(trip);

            tripService.create(trip,user);

            Mockito.verify(mockRepository, Mockito.times(1)).create(trip);
    }

    @Test
    public void create_Should_Throw_When_UserIsBlocked () {
        Trip trip = createMockTrip();
        User user = createMockUser();
        user.setBlocked(true);

        Assertions.assertThrows(AuthorizationException.class, () -> tripService.create(trip,user));

    }




}
