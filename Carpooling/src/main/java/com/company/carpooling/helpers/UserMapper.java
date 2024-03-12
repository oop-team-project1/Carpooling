package com.company.carpooling.helpers;

import com.company.carpooling.models.User;
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
}
