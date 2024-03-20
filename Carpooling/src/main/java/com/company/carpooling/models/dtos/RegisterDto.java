package com.company.carpooling.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RegisterDto{
    @NotEmpty(message = "First name can't be empty!")
    @Size(min = 4, max = 32, message = "First name must be between 4 symbols and 32 symbols.")
    private String firstName;
    @NotEmpty(message = "Last name can't be empty!")
    @Size(min = 4, max = 32, message = "Last name must be between 4 symbols and 32 symbols.")
    private String lastName;
    @NotEmpty(message = "Username can't be empty!")
    private String username;
    @NotEmpty(message = "Email can't be empty!")
    @Pattern(
            regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "Please provide a valid email address. The email format should be like user@example.com"
    )
    private String email;
    @NotEmpty(message = "Phone number can't be empty")
    private String phoneNumber;
    @NotEmpty(message = "Password can't be empty.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+*&^%$#@]).{8,}$",
            message = "Password must be at least 8 symbols, " +
                    "should contain capital letter, digit and special symbol (+, -, *, &, ^, …)")
    private String password;
    @NotEmpty(message = "Password confirmation can't be empty!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+*&^%$#@]).{8,}$",
            message = "Password must be at least 8 symbols, " +
                    "should contain capital letter, digit and special symbol (+, -, *, &, ^, …)")
    private String passwordConfirm;
}
