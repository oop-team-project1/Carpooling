package com.company.carpooling;

import com.company.carpooling.helpers.FilterOptionsTrip;
import com.company.carpooling.models.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
                LocalDateTime.now(),
                Date.valueOf(LocalDate.now()),
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
        mockTrip.setPassengersCount(3);
        mockTrip.setStatus(1);
        mockTrip.setDateOfCreation(Date.valueOf(LocalDate.now()));
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

    //TODO: remove when merge branches
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
}
