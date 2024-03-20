package com.company.carpooling.services;

import com.company.carpooling.exceptions.AuthorizationException;
import com.company.carpooling.helpers.filters.FilterOptionsTrip;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.User;
import com.company.carpooling.repositories.contracts.TripRepository;
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

        Assertions.assertEquals(mockTrip, result);
    }

    @Test
    public void create_Should_CallRepository_When_UserIsNotBlocked() {
        Trip trip = createMockTrip();
        User user = createMockUser();

        Mockito.doNothing().when(mockRepository).create(trip);

        tripService.create(trip, user);

        Mockito.verify(mockRepository, Mockito.times(1)).create(trip);
    }

    @Test
    public void create_Should_Throw_When_UserIsBlocked() {
        Trip trip = createMockTrip();
        User user = createMockUser();
        user.setBlocked(true);

        Assertions.assertThrows(AuthorizationException.class, () -> tripService.create(trip, user));

    }

    @Test
    public void update_Should_CallRepository_When_UserIsDriver() {
        Trip mockTrip = createMockTrip();
        User driver = mockTrip.getDriver();

        tripService.update(mockTrip, driver);

        Mockito.verify(mockRepository, Mockito.times(1)).update(mockTrip);
    }

    @Test
    public void update_Should_Throw_When_UserIsBlocked() {
        Trip mockTrip = createMockTrip();
        User driver = mockTrip.getDriver();
        driver.setBlocked(true);

        Assertions.assertThrows(AuthorizationException.class, () -> tripService.update(mockTrip, driver));
    }

    @Test
    public void update_Should_Throw_When_UserIsNotDriver() {
        Trip mockTrip = createMockTrip();
        User driver = createMockUser();
        driver.setId(2);

        Assertions.assertThrows(AuthorizationException.class, () -> tripService.update(mockTrip, driver));
    }

    @Test
    public void cancelTrip_Should_CallRepository_When_UserIsDriver() {
        Trip mockTrip = createMockTrip();
        User mockDriver = mockTrip.getDriver();

        Mockito.when(mockRepository.get(Mockito.anyInt())).thenReturn(mockTrip);

        tripService.cancelTrip(mockTrip.getId(), mockDriver);

        Mockito.verify(mockRepository, Mockito.times(1)).update(mockTrip);
    }

    @Test
    public  void cancelTrip_Should_CallRepository_When_UserIsAdmin() {
        User mockUserAdmin = createMockAdmin();
        Trip mockTrip = createMockTrip();

        Mockito.when(mockRepository.get(Mockito.anyInt())).thenReturn(mockTrip);

        tripService.cancelTrip(mockTrip.getId(), mockUserAdmin);

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockTrip);
    }

    @Test
    void delete_Should_ThrowException_When_UserIsNotAdminOrCreator() {
        // Arrange
        Trip mockTrip = createMockTrip();
        User driver = mockTrip.getDriver();
        driver.setId(1);

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockTrip);

        User mockUser = createMockUser();
        mockUser.setId(2);

        // Act, Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> tripService.cancelTrip(mockTrip.getId(), mockUser));
    }

}
