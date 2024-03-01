package com.company.carpooling.helpers;

import com.company.carpooling.models.*;
import com.company.carpooling.services.CityService;
import com.company.carpooling.services.CountryService;
import com.company.carpooling.services.StreetService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AddressMapper {
    private final CountryService countryService;
    private final CityService cityService;
    private final StreetService streetService;

    public Street getAddress(AddressDto addressDto) {
        Country country = new Country();
        country.setName(addressDto.getCountry());
        Country countrySaved = countryService.fetchOrCreate(country);

        City city = new City();
        city.setName(addressDto.getCity());
        city.setCountry(country);
        City citySaved = cityService.fetchOrCreate(city, countrySaved);

        Street street = new Street();
        street.setStreetName(addressDto.getStreet());
        street.setCity(city);

        return streetService.fetchOrCreate(street, citySaved);


    }


}
