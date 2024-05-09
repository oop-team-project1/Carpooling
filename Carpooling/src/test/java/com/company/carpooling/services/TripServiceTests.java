package com.company.carpooling.services;

import com.company.carpooling.exceptions.AuthorizationException;
import com.company.carpooling.exceptions.EntityDuplicateException;
import com.company.carpooling.helpers.filters.FilterOptionsTrip;
import com.company.carpooling.models.Application;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.User;
import com.company.carpooling.repositories.contracts.TripRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

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


    @Test
    void getApplications_Should_Throw_When_UserIsNotDriver(){
        // Arrange
        Trip mockTrip = createMockTrip();
        mockTrip.getDriver().setId(2);
        User driver = createMockUser();
        // Act
        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockTrip);
        // Assert
        Assertions.assertThrows(AuthorizationException.class, () -> tripService.getApplications(1,driver));
    }
    @Test
    void getApplications_Should_Throw_When_UserIsBlocked(){
        // Arrange
        Trip mockTrip = createMockTrip();
        mockTrip.getDriver().setBlocked(true);
        // Act
        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockTrip);
        // Assert
        Assertions.assertThrows(AuthorizationException.class, () -> tripService.getApplications(1,mockTrip.getDriver()));
    }
    @Test
    void getApplications_Should_Return_Applications_WhenUserIsDriver(){
        // Arrange
        Trip mockTrip = Mockito.spy(createMockTrip());
        User driver = createMockUser();

        // Act
        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockTrip);

        tripService.getApplications(1,driver);
        // Assert
        Mockito.verify(mockTrip,Mockito.times(1)).getApplications();
    }

    @Test
    void applyForTrip_Should_Throw_When_UserIsBlocked(){
        // Arrange
        User user = createMockUser();
        user.setBlocked(true);
        // Act & Assert
        Assertions.assertThrows(AuthorizationException.class, () -> tripService.applyForTrip(1,user));
    }
    @Test
    void applyForTrip_Should_Throw_When_UserIsDriver(){
        // Arrange
        Trip mockTrip = createMockTrip();
        User driver = createMockUser();

        // Act
        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockTrip);

        // Assert
        Assertions.assertThrows(AuthorizationException.class, () -> tripService.applyForTrip(1,driver));
    }

    @Test
    void applyForTrip_Should_Throw_When_UserAlreadyApplied(){
        // Arrange
        Trip mockTrip = createMockTrip();
        mockTrip.getDriver().setId(2);
        User driver = createMockUser();
        Set<Application> applications = new HashSet<>();
        applications.add(createMockApplication());
        mockTrip.setApplications(applications);

        // Act
        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockTrip);

        // Assert
        Assertions.assertThrows(EntityDuplicateException.class, () -> tripService.applyForTrip(1,driver));
    }


    @Test
    public void applyForTrip_Should_CallRepository() {
        User mockUser = createMockUser();
        Trip mockTrip = createMockTrip();
        mockTrip.getDriver().setId(2);

        Mockito.when(mockRepository.get(Mockito.anyInt())).thenReturn(mockTrip);

        tripService.applyForTrip(mockTrip.getId(), mockUser);

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockTrip);
    }
    @Test
    public void cancelParticipation_Should_CallRepository() {
        User mockUser = createMockUser();
        Trip mockTrip = createMockTrip();

        Mockito.when(mockRepository.get(Mockito.anyInt())).thenReturn(mockTrip);

        tripService.cancelParticipation(mockTrip.getId(), mockUser);

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockTrip);
    }

    @Test
    public void approvePassengers_Should_Throw_When_UserIsBlocked() {
        User driver = createMockUser();
        driver.setBlocked(true);

        Assertions.assertThrows(AuthorizationException.class, () -> tripService.approvePassenger(1,driver, 1));

    }





}
