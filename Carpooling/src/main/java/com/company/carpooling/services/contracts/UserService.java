package com.company.carpooling.services.contracts;

import com.company.carpooling.helpers.filters.FilterOptionsUsers;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.User;

import java.util.List;

public interface UserService {
    List<User> getAll(FilterOptionsUsers filterOptionsUsers);
    List<Trip> getTripsAsPassenger(User user);
    List<Trip> getPendingRides(User user);

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByPhoneNumber(String phoneNumber);

    void blockUser(int id, User user);

    void unblockUser(int id, User user);

    void create(User userToCreate);

    void update(User userToUpdate, User user);

    void makeAdmin(int id, User user);

    void removeAdmin(int id, User user);

    void deleteUser(int id, User user);

    void addProfilePicture(int id, User user, String newAvatar);

    void activateAccount(int code);

    void resendActivationCode(String username);
    void sendActivationEmail(User user);
}
