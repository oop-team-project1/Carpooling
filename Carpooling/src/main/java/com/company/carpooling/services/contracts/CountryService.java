package com.company.carpooling.services.contracts;

import com.company.carpooling.models.Country;

public interface CountryService {
    Country fetchOrCreate(Country country);
}
