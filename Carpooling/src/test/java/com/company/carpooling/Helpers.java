package com.company.carpooling;

import com.company.carpooling.helpers.filters.FilterOptionsTrip;
import com.company.carpooling.helpers.filters.FilterOptionsUsers;
import com.company.carpooling.models.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Helpers {
    public static FilterOptionsTrip createMockFilterOptionsForTrip() {
        return new FilterOptionsTrip(
                "status",
                "startStreet",
                "startCity",
                "startCountry",
                "endStreet",
                "endCity",
                "endCountry",
                "username",
                2,
                LocalTime.now().toString(),
                LocalDate.now().toString(),
                LocalDate.now().toString(),
                "startPointCity",
                "desc"
        );
    }

    public static Trip createMockTrip() {
        Trip mockTrip = new Trip();
        mockTrip.setId(1);
        mockTrip.setDriver(createMockUser());
        mockTrip.setStartPoint(createMockPoint(1));
        mockTrip.setEndPoint(createMockPoint(2));
        mockTrip.setDepartureTime(LocalDateTime.now());
        mockTrip.setDistance(15.75f);
        mockTrip.setDuration(3.4f);
        mockTrip.setSeatsAvailable(3);
        mockTrip.setStatus(new TripStatus(1,"Upcoming"));
        mockTrip.setDateOfCreation(Date.valueOf(LocalDate.now()).toLocalDate());
        return mockTrip;
    }

    public static Street createMockPoint(int id) {
        Street mockStreet = new Street();
        mockStreet.setId(id);
        mockStreet.setStreetName("Street");
        mockStreet.setCity(createMockCity(id));
        return mockStreet;
    }

    public static City createMockCity(int id) {
        City mockCity = new City();
        mockCity.setId(id);
        mockCity.setName("City");
        mockCity.setCountry(createMockCountry(id));
        return mockCity;
    }

    public static Country createMockCountry(int id) {
        Country mockCountry = new Country();
        mockCountry.setId(id);
        mockCountry.setName("Country");
        return mockCountry;
    }

    public static User createMockUser() {
        User user = new User();
        user.setId(1);
        user.setFirstName("name");
        user.setLastName("name");
        user.setUsername("username");
        user.setPassword("pass");
        user.setEmail("email@test.com");
        user.setAdmin(false);
        user.setBlocked(false);
        user.setProfilePic(createMockProfilePic());
        user.setPhoneNumber("088745696");
        return user;
    }

    public static UserProfilePic createMockProfilePic() {
        UserProfilePic pic = new UserProfilePic();
        pic.setPicId(1);
        pic.setPic("url");
        return pic;

    }

    public static User createMockAdmin() {
        User admin = createMockUser();
        admin.setAdmin(true);
        return admin;
    }

    public static FilterOptionsUsers createMockFilterOptionsUsers() {
        return new FilterOptionsUsers(
                "username",
                "0000000000",
                "email@abv.bg",
                "username",
                "desc");
    }
}
