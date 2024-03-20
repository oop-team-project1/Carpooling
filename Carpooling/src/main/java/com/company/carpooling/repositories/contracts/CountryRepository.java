package com.company.carpooling.repositories.contracts;

import com.company.carpooling.models.Country;

public interface CountryRepository {
    Country getByName(String country);
    Country create(Country country);
}
