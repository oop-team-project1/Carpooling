package com.company.carpooling.services;

import com.company.carpooling.exceptions.*;
import com.company.carpooling.helpers.filters.FilterOptionsUsers;
import com.company.carpooling.models.User;
import com.company.carpooling.models.UserProfilePic;
import com.company.carpooling.repositories.contracts.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.company.carpooling.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;


    @Test
    public void getAll_Should_CallRepository() {
        FilterOptionsUsers mockFilterOptions = createMockFilterOptionsUsers();
        List<User> users = new ArrayList<>();

        Mockito.when(userRepository.getAll(mockFilterOptions)).thenReturn(users);

        Assertions.assertEquals(userRepository.getAll(mockFilterOptions), users);
    }

    @Test
    public void getById_Should_ReturnUser_When_MatchExists() {
        Mockito.when(userRepository.getById(1))
                .thenReturn(createMockUser());

        User result = userService.getById(1);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("username", result.getUsername());
        Assertions.assertEquals("pass", result.getPassword());
        Assertions.assertEquals("name", result.getFirstName());
        Assertions.assertEquals("name", result.getLastName());
        Assertions.assertEquals("088745696", result.getPhoneNumber());
        Assertions.assertEquals("email@test.com", result.getEmail());
    }

    @Test
    public void getByName_Should_ReturnUser_When_MatchExists() {
        Mockito.when(userRepository.getByUsername(createMockUser().getUsername()))
                .thenReturn(createMockUser());

        User result = userService.getByUsername(createMockUser().getUsername());

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("username", result.getUsername());
        Assertions.assertEquals("pass", result.getPassword());
        Assertions.assertEquals("name", result.getFirstName());
        Assertions.assertEquals("name", result.getLastName());
        Assertions.assertEquals("088745696", result.getPhoneNumber());
        Assertions.assertEquals("email@test.com", result.getEmail());
    }

    @Test
    public void getByEmail_Should_ReturnUser_When_MatchExists() {
        Mockito.when(userRepository.getByEmail(createMockUser().getEmail()))
                .thenReturn(createMockUser());

        User result = userService.getByEmail(createMockUser().getEmail());

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("username", result.getUsername());
        Assertions.assertEquals("pass", result.getPassword());
        Assertions.assertEquals("name", result.getFirstName());
        Assertions.assertEquals("name", result.getLastName());
        Assertions.assertEquals("088745696", result.getPhoneNumber());
        Assertions.assertEquals("email@test.com", result.getEmail());
    }

    @Test
    public void create_Should_CallRepository_When_UserDoesNotExists() {
        User mockUser = createMockUser();

        Mockito.when(userRepository.getByUsername(mockUser.getUsername()))
                .thenThrow(new EntityNotFoundException("User", "username", mockUser.getUsername()));

        Mockito.when(userRepository.getByEmail(mockUser.getEmail()))
                .thenThrow(new EntityNotFoundException("User", "email", mockUser.getEmail()));

        Mockito.when(userRepository.getByPhoneNumber(mockUser.getPhoneNumber()))
                .thenThrow(new EntityNotFoundException("User", "phone number", mockUser.getPhoneNumber()));

        userService.create(mockUser);

        Mockito.verify(userRepository, Mockito.times(1))
                .create(mockUser);
    }

    @Test
    public void create_Should_Throw_When_UserWithSameUsernameExists() {
        User mockUser = createMockUser();

        Mockito.when(userRepository.getByUsername(mockUser.getUsername()))
                .thenReturn(mockUser);

        Assertions.assertThrows(EntityDuplicateException.class,
                () -> userService.create(mockUser));
    }

    @Test
    public void create_Should_Throw_When_UserWithSameEmailExists() {
        User mockUser = createMockUser();

        Mockito.when(userRepository.getByEmail(mockUser.getEmail()))
                .thenReturn(mockUser);

        Assertions.assertThrows(EntityDuplicateException.class,
                () -> userService.create(mockUser));
    }

    @Test
    public void create_Should_Throw_When_UserWithSamePhoneNumberExists() {
        User mockUser = createMockUser();

        Mockito.when(userRepository.getByPhoneNumber(mockUser.getPhoneNumber()))
                .thenReturn(mockUser);

        Assertions.assertThrows(EntityDuplicateException.class,
                () -> userService.create(mockUser));
    }

    @Test
    public void create_Should_Throw_When_UserEmailIsNotValid() {
        User mockUser = createMockUser();
        mockUser.setEmail(".");

        Mockito.when(userRepository.getByUsername(mockUser.getUsername()))
                .thenReturn(mockUser);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.create(mockUser));
    }

    @Test
    public void update_Should_CallRepository_When_UserDoesNotExists() {
        User mockUser = createMockUser();

        Mockito.when(userRepository.getByUsername(mockUser.getUsername()))
                .thenThrow(new EntityNotFoundException("User", "username", mockUser.getUsername()));

        userService.update(mockUser, createMockUser());

        Mockito.verify(userRepository, Mockito.times(1))
                .update(mockUser);
    }

    @Test
    public void update_Should_Throw_When_UserWithSameUsernameExists() {
        User mockUser = createMockUser();
        User mockUser2 = createMockUser();
        mockUser2.setId(2);

        Mockito.when(userRepository.getByUsername(mockUser.getUsername()))
                .thenReturn(mockUser2);

        Assertions.assertThrows(EntityDuplicateException.class,
                () -> userService.update(mockUser, createMockUser()));
    }

    @Test
    public void update_Should_Throw_When_UserIsBlocked() {
        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        User mockUser2 = createMockUser();
        mockUser2.setId(2);

        Assertions.assertThrows(AuthorizationException.class,
                () -> userService.update(mockUser2, mockUser));
    }

    @Test
    public void blockUser_Should_Block_When_UserIsNotBlocked() {
        User mockUser = createMockUser();
        User mockUser2 = createMockUser();
        mockUser2.setAdmin(true);
        mockUser2.setId(2);

        Mockito.when(userRepository.getById(mockUser.getId()))
                .thenReturn(mockUser);

        userService.blockUser(mockUser.getId(), mockUser2);

        Assertions.assertTrue(mockUser.isBlocked());
    }

    @Test
    public void blockUser_Should_Throw_When_UserIsAlreadyBlocked() {
        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        User mockUser2 = createMockUser();
        mockUser2.setAdmin(true);
        mockUser2.setId(2);

        Mockito.when(userRepository.getById(mockUser.getId()))
                .thenReturn(mockUser);

        Assertions.assertThrows(BlockedUnblockedUserException.class,
                () -> userService.blockUser(mockUser.getId(), mockUser2));
    }

    @Test
    public void blockUser_Should_Throw_When_UserIsNotAdmin() {
        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        User mockUser2 = createMockUser();
        mockUser2.setId(2);

        Assertions.assertThrows(AuthorizationException.class,
                () -> userService.blockUser(mockUser.getId(), mockUser2));
    }

    @Test
    public void unblockUser_Should_Unblock_When_UserIsBlocked() {
        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        User mockUser2 = createMockUser();
        mockUser2.setAdmin(true);
        mockUser2.setId(2);

        Mockito.when(userRepository.getById(mockUser.getId()))
                .thenReturn(mockUser);

        userService.unblockUser(mockUser.getId(), mockUser2);

        Assertions.assertFalse(mockUser.isBlocked());
    }

    @Test
    public void unblockUser_Should_Throw_When_UserIsAlreadyUnblocked() {
        User mockUser = createMockUser();
        User mockUser2 = createMockUser();
        mockUser2.setAdmin(true);
        mockUser2.setId(2);

        Mockito.when(userRepository.getById(mockUser.getId()))
                .thenReturn(mockUser);

        Assertions.assertThrows(BlockedUnblockedUserException.class,
                () -> userService.unblockUser(mockUser.getId(), mockUser2));
    }

    @Test
    public void unblockUser_Should_Throw_When_UserIsNotAdmin() {
        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        User mockUser2 = createMockUser();
        mockUser2.setId(2);

        Assertions.assertThrows(AuthorizationException.class,
                () -> userService.unblockUser(mockUser.getId(), mockUser2));
    }

    @Test
    public void makeAdmin_Should_Throw_When_UserIsNotAdmin() {
        User mockUser = createMockUser();
        User mockUser2 = createMockUser();
        mockUser2.setId(2);

        Assertions.assertThrows(AuthorizationException.class,
                () -> userService.makeAdmin(mockUser.getId(), mockUser2));
    }

    @Test
    public void makeAdmin_Should_MakeAdmin_When_UserIsNotAdmin() {
        User mockUser = createMockUser();
        User mockUser2 = createMockUser();
        mockUser2.setAdmin(true);
        mockUser2.setId(2);

        Mockito.when(userRepository.getById(mockUser.getId()))
                .thenReturn(mockUser);

        userService.makeAdmin(mockUser.getId(), mockUser2);

        Assertions.assertTrue(mockUser.isAdmin());
    }

    @Test
    public void removeAdmin_Should_Throw_When_UserToMakeIsAlreadyAdmin() {
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        User mockUser2 = createMockUser();
        mockUser2.setAdmin(true);
        mockUser2.setId(2);

        Mockito.when(userRepository.getById(mockUser.getId()))
                .thenReturn(mockUser);

        Assertions.assertThrows(AdminException.class,
                () -> userService.makeAdmin(mockUser.getId(), mockUser2));
    }

    @Test
    public void removeAdmin_Should_Throw_When_UserIsNotAdmin() {
        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        User mockUser2 = createMockUser();
        mockUser2.setId(2);

        Assertions.assertThrows(AuthorizationException.class,
                () -> userService.removeAdmin(mockUser.getId(), mockUser2));
    }

    @Test
    public void removeAdmin_Should_RemoveAdmin_When_UserIsAdmin() {
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        User mockUser2 = createMockUser();
        mockUser2.setAdmin(true);
        mockUser2.setId(2);

        Mockito.when(userRepository.getById(mockUser.getId()))
                .thenReturn(mockUser);

        userService.removeAdmin(mockUser.getId(), mockUser2);

        Assertions.assertFalse(mockUser.isAdmin());
    }

    @Test
    public void removeAdmin_Should_Throw_When_UserToRemoveIsNotAdmin() {
        User mockUser = createMockUser();
        mockUser.setAdmin(false);
        User mockUser2 = createMockUser();
        mockUser2.setAdmin(true);
        mockUser2.setId(2);

        Mockito.when(userRepository.getById(mockUser.getId()))
                .thenReturn(mockUser);

        Assertions.assertThrows(AdminException.class,
                () -> userService.removeAdmin(mockUser.getId(), mockUser2));
    }

    @Test
    public void deleteUser_Should_Throw_When_UserIsNotAdmin() {
        User mockUser = createMockUser();
        User mockUser2 = createMockUser();
        mockUser2.setId(2);

        Assertions.assertThrows(AuthorizationException.class,
                () -> userService.deleteUser(mockUser.getId(), mockUser2));
    }

    @Test
    public void deleteUser_Should_Delete_When_UserExists() {
        User mockUser = createMockUser();
        User mockUser2 = createMockUser();
        mockUser2.setId(2);
        mockUser2.setAdmin(true);

        userService.deleteUser(mockUser.getId(), mockUser2);

        Mockito.verify(userRepository, Mockito.times(1))
                .deleteUser(mockUser.getId());
    }

    @Test
    public void addProfilePicture_Should_Throw_When_UserIsNotOwner() {
        User mockUser = createMockUser();

        Assertions.assertThrows(AuthorizationException.class,
                () -> userService.addProfilePicture(2, mockUser, ""));
    }

    @Test
    public void addProfilePicture_Should_Add() {
        User mockUser = createMockUser();
        UserProfilePic userProfilePic = createMockProfilePic();
        mockUser.setProfilePic(userProfilePic);

        userService.addProfilePicture(userProfilePic.getPicId(), mockUser, "");

        Assertions.assertEquals("", mockUser.getProfilePic().getPic());
    }
}
