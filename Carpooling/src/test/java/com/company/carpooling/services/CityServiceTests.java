package com.company.carpooling.services;

import com.company.carpooling.models.City;
import com.company.carpooling.repositories.contracts.CityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.company.carpooling.Helpers.createMockCity;

@ExtendWith(MockitoExtension.class)
public class CityServiceTests {
    @Mock
    CityRepository mockRepository;

    @InjectMocks
    CityServiceImpl cityService;

    @Test
    public void fetchOrCreate_Should_Return_City_When_MatchByNameExist () {
        City mockCity = createMockCity(1);

        Mockito.when(mockRepository.getByName(mockCity.getName(), mockCity.getCountry()))
                .thenReturn(mockCity);

        City result = cityService.fetchOrCreate(mockCity, mockCity.getCountry());
        Assertions.assertEquals(mockCity,result);
    }


}
