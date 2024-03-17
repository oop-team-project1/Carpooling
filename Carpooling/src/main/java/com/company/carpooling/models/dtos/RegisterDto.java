package com.company.carpooling.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto extends LoginDto{
    @NotEmpty(message = "Username can't be empty!")
    private String username;
    @NotEmpty(message = "Password confirmation can't be empty!")
    private String passwordConfirm;
    @NotEmpty(message = "First name can't be empty!")
    @Size(min = 4, max = 32, message = "First name must be between 4 symbols and 32 symbols.")
    private String firstName;
    @NotEmpty(message = "Last name can't be empty!")
    @Size(min = 4, max = 32, message = "Last name must be between 4 symbols and 32 symbols.")
    private String lastName;
    @NotEmpty(message = "Phone number can't be empty")
    private String phoneNumber;
}
