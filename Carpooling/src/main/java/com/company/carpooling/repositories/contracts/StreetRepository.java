package com.company.carpooling.repositories.contracts;

import com.company.carpooling.models.City;
import com.company.carpooling.models.Street;

public interface StreetRepository {

    Street getByName(String cityName, City city);

    Street create(Street street, City city);
}
