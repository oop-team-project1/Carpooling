package com.company.carpooling.services.contracts;

import com.company.carpooling.models.City;
import com.company.carpooling.models.Street;

public interface StreetService {
    Street fetchOrCreate(Street street, City city);
}
