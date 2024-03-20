package com.company.carpooling.repositories.contracts;

import com.company.carpooling.models.City;
import com.company.carpooling.models.Country;

public interface CityRepository {
    City getByName(String cityName, Country country);

    City create(City city,Country country);
}
