package com.company.carpooling.models.dtos;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    @NotEmpty(message = "First name can't be empty")
    @Size(min = 2, max = 20, message = "First name must be between 2 and 20 symbols")
    private String firstName;

    @NotEmpty(message = "Last name can't be empty")
    @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 symbols")
    private String lastName;

    @NotEmpty(message = "Password can't be empty")
    private String password;

    @NotEmpty(message = "Email can't be empty")
    private String email;

    @NotEmpty(message = "Username can't be empty")
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 symbols")
    private String username;

    @NotEmpty(message = "Phone number can't be empty")
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits and unique")
    private String phoneNumber;
}
