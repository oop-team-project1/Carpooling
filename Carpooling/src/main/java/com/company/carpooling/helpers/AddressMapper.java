package com.company.carpooling.helpers;

import com.company.carpooling.models.*;
import com.company.carpooling.models.dtos.AddressDto;
import com.company.carpooling.models.json.Point;
import com.company.carpooling.services.BingMapsService;
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
    private final BingMapsService bingMapsService;

    public Street getAddress(Point coordinates) {

        AddressDto unifiedAddress = bingMapsService.getAddress(coordinates);

        Country country = new Country();
        country.setName(unifiedAddress.getCountry());
        Country countrySaved = countryService.fetchOrCreate(country);

        City city = new City();
        city.setName(unifiedAddress.getCity());
        city.setCountry(country);
        City citySaved = cityService.fetchOrCreate(city, countrySaved);

        Street street = new Street();
        street.setStreetName(unifiedAddress.getStreet());
        street.setCity(city);

        return streetService.fetchOrCreate(street, citySaved);


    }


}
