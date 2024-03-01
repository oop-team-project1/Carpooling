package com.company.carpooling.repositories;

import com.company.carpooling.models.Country;

public interface CountryRepository {
    Country getByName(String country);
    Country create(Country country);
}
