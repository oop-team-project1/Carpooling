package com.company.carpooling.helpers;

import com.company.carpooling.models.User;
import com.company.carpooling.models.UserDto;
import com.company.carpooling.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final UserService userService;

    @Autowired
    public UserMapper(UserService userService) {
        this.userService = userService;
    }

    public User fromDto(int id, UserDto userDto) {
        User user = fromDto(userDto);
        user.setUsername(userService.getById(id).getUsername());
        user.setId(id);
        user.setAdmin(false);
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
        return user;
    }
}
