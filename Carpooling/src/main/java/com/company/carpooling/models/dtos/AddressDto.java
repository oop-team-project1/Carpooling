package com.company.carpooling.models.dtos;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class AddressDto {

    String street;

    String city;
    @Size(min=2, max = 3, message = "Country should be provided in ISO format, example:UK,LUX,ET")
    String country;
}
