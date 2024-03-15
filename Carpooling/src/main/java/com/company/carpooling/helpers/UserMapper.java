package com.company.carpooling.helpers;

import com.company.carpooling.models.User;
import com.company.carpooling.models.dtos.RegisterDto;
import com.company.carpooling.models.dtos.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User fromDto(int id, UserDto userDto) {
        User user = fromDto(userDto);
        user.setId(id);
        return user;
    }

    public User fromDto(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAdmin(false);
        return user;
    }

    public User fromDtoRegister(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setPassword(registerDto.getPassword());
        user.setEmail(registerDto.getEmail());
        return user;
    }
}
