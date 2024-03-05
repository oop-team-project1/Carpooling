package com.company.carpooling;

import com.company.carpooling.helpers.FilterOptionsTrip;

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
}
