package com.company.carpooling.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FilterDtoUser {
    private String username;
    private String phoneNumber;
    private String email;
    private String sortBy;
    private String sortOrder;
}
