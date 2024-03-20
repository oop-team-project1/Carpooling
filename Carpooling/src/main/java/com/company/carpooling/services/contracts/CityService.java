package com.company.carpooling.services.contracts;

import com.company.carpooling.models.City;
import com.company.carpooling.models.Country;

public interface CityService {

    City fetchOrCreate(City city, Country country);
}
