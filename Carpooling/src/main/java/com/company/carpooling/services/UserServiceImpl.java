package com.company.carpooling.services;

import com.company.carpooling.exceptions.*;
import com.company.carpooling.helpers.FilterOptionsUsers;
import com.company.carpooling.models.*;
import com.company.carpooling.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    public static final String MODIFY_PROFILE_PICTURE_ERROR = "You can't modify profile picture!";
    private final UserRepository repository;
    private final UserProfilePicService userProfilePicService;
    private final EmailService emailService;
    private final ActivationCodeService activationCodeService;
    private final Random random = new Random();

    public static final String PERMISSION_ERROR = "Only admin can delete user.";
    public static final String USER_IS_BLOCKED = "User is blocked";

    @Override
    public List<User> getAll(FilterOptionsUsers filterOptionsUsers) {
        return repository.getAll(filterOptionsUsers);
    }

    @Override
    public List<Trip> getTripsAsPassenger(User user) {
        if(user.getApplications() == null) {
            return Collections.emptyList();
        }
       List<Application> applications = user.getApplications()
                                            .stream()
                                            .filter(application -> application.getStatus()
                                                    .equals(new PassengerStatus(2, "Approved")))
                                            .toList();
        return applications
                .stream()
                .map(Application::getTrip).toList();
    }

    @Override
    public List<Trip> getPendingRides(User user) {
        if(user.getApplications() == null) {
            return Collections.emptyList();
        }
        List<Application> applications = user.getApplications()
                .stream()
                .filter(application -> application.getStatus()
                        .equals(new PassengerStatus(1, "Pending")))
                .toList();
        return applications
                .stream()
                .map(Application::getTrip).toList();
    }

    @Override
    public User getById(int id) {
        return repository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return repository.getByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        return repository.getByEmail(email);
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        return repository.getByPhoneNumber(phoneNumber);
    }

    @Override
    public void create(User userToCreate) {
        boolean duplicateUsernameExists = true;
        boolean duplicateEmailExists = true;
        boolean duplicatePhoneNumberExists = true;

        try {
            repository.getByUsername(userToCreate.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateUsernameExists = false;
        }

        try {
            repository.getByEmail(userToCreate.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateEmailExists = false;
        }

        try {
            repository.getByPhoneNumber(userToCreate.getPhoneNumber());
        } catch (EntityNotFoundException e) {
            duplicatePhoneNumberExists = false;
        }

        if (!isValidEmail(userToCreate.getEmail())) {
            throw new IllegalArgumentException("The email is not valid!");
        }

        if (duplicateUsernameExists) {
            throw new EntityDuplicateException("User", "username", userToCreate.getUsername());
        } else if (duplicateEmailExists) {
            throw new EntityDuplicateException("User", "email", userToCreate.getEmail());
        } else if (duplicatePhoneNumberExists) {
            throw new EntityDuplicateException("User", "phone number", userToCreate.getPhoneNumber());
        }

        userToCreate.setVerified(false);
        repository.create(userToCreate);
        sendActivationEmail(userToCreate);
    }

    @Override
    public void update(User userToUpdate, User user) {
        checkIfBlocked(user);
        checkIfUsernameIsUnique(userToUpdate);
        checkIfEmailIsUnique(userToUpdate);
        checkIfPhoneNumberIsUnique(userToUpdate);
        repository.update(userToUpdate);
    }

    @Override
    public void blockUser(int id, User user) {
        checkModifyPermissions(user);
        User userToBlock = repository.getById(id);
        try {
            checkIfBlocked(userToBlock);
        } catch (AuthorizationException ignored) {
            throw new BlockedUnblockedUserException(userToBlock.getId(), "blocked");
        }
        userToBlock.setBlocked(true);
        repository.update(userToBlock);
    }

    @Override
    public void unblockUser(int id, User user) {
        checkModifyPermissions(user);
        User userToUnblock = repository.getById(id);
        try {
            checkIfBlocked(userToUnblock);
        } catch (AuthorizationException ignored) {
            userToUnblock.setBlocked(false);
            repository.update(userToUnblock);
            return;
        }
        throw new BlockedUnblockedUserException(userToUnblock.getId(), "unblocked");
    }

    @Override
    public void makeAdmin(int id, User user) {
        checkModifyPermissions(user);
        User userToAdmin = repository.getById(id);
        try {
            checkModifyPermissions(userToAdmin);
        } catch (AuthorizationException ignored) {
            userToAdmin.setAdmin(true);
            repository.update(userToAdmin);
            return;
        }
        throw new AdminException(userToAdmin.getId(), "admin");
    }

    @Override
    public void removeAdmin(int id, User user) {
        checkModifyPermissions(user);
        User userToAdmin = repository.getById(id);
        try {
            checkModifyPermissions(userToAdmin);
        } catch (AuthorizationException e) {
            throw new AdminException(userToAdmin.getId(), "removed admin");
        }
        userToAdmin.setAdmin(false);
        repository.update(userToAdmin);
    }

    @Override
    public void deleteUser(int id, User user) {
        checkModifyPermissions(user);
        repository.deleteUser(id);
    }

    @Override
    public void addProfilePicture(int id, User user, String newAvatar) {
        if (user.getId() != id) {
            throw new AuthorizationException(MODIFY_PROFILE_PICTURE_ERROR);
        }

        UserProfilePic profilePic = new UserProfilePic();
        profilePic.setPic(newAvatar);
        user.setProfilePic(profilePic);
        repository.update(user);
    }

    @Override
    public void activateAccount(int code) {
        try {
            ActivationCode activationCode = activationCodeService.getByCode(code);
            User user = repository.getByEmail(activationCode.getEmail());
            user.setVerified(true);
            activationCodeService.deleteActivationCodeByUser(user.getEmail());
            repository.update(user);
        } catch (EntityNotFoundException e) {
            throw new WrongActivationCodeException("Code not active. Maybe user is activated already.");
        }
    }

    @Override
    public void resendActivationCode(String username) {
        User user = repository.getByUsername(username);
        if (user.isVerified())
            throw new ForbiddenOperationException("User already activated!");
        sendActivationEmail(user);
    }
    @Override
    public void sendActivationEmail(User user){
        String email = user.getEmail();
        int code = getActivationCode(user);
        emailService.sendEmail(email, "Account activation", String.valueOf(code));
        emailService.sendUserCreationVerificationCode(user, code);
        System.out.println(code);
    }
    private int getActivationCode(User user) {
        ActivationCode activationCode;
        int code;
        do {
            code = getCode();
            try {
                activationCode = activationCodeService.getByCode(code);
            } catch (EntityNotFoundException ignored) {
                activationCode = null;
            }
        } while (activationCode != null);

        activationCode = new ActivationCode();
        activationCode.setActivationCode(code);
        activationCode.setEmail(user.getEmail());
        activationCodeService.create(activationCode);

        return code;
    }
    private int getCode() {
        return 1000 + random.nextInt(9000);
    }

    private void checkModifyPermissions(User user) {
        if (!(user.isAdmin())) {
            throw new AuthorizationException(PERMISSION_ERROR);
        }
    }

    private void checkIfBlocked(User user) {
        if (user.isBlocked()) {
            throw new AuthorizationException(USER_IS_BLOCKED);
        }
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void checkIfUsernameIsUnique(User user) {
        boolean duplicateExists = true;
        try {
            User existingUser = repository.getByUsername(user.getUsername());
            if (existingUser.getId() == user.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }
    }

    private void checkIfEmailIsUnique(User user) {
        boolean duplicateExists = true;

        try {
            User existingUser = repository.getByEmail(user.getEmail());
            if(existingUser.getId() == user.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }
    }

    private void checkIfPhoneNumberIsUnique(User user) {
        boolean duplicateExists = true;
        try {
            User existingUser = repository.getByPhoneNumber(user.getPhoneNumber());
            if(existingUser.getId() == user.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "phone number", user.getPhoneNumber());
        }
    }
}
