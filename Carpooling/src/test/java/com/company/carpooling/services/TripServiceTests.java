package com.company.carpooling.services;

import com.company.carpooling.helpers.FilterOptionsTrip;
import com.company.carpooling.repositories.TripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.company.carpooling.Helpers.createMockFilterOptionsForTrip;

@ExtendWith(MockitoExtension.class)
public class TripServiceTests {
    @Mock
    TripRepository mockRepository;

    @InjectMocks
    TripServiceImpl tripService;

    @Test
    public void get_Should_CallRepository() {
        FilterOptionsTrip mockFilterOptions = createMockFilterOptionsForTrip();
        Mockito.when(mockRepository.get(mockFilterOptions)).thenReturn(null);

        tripService.get(mockFilterOptions);

        Mockito.verify(mockRepository, Mockito.times(1)).get(mockFilterOptions);
    }


}
