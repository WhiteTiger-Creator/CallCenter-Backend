package org.comcom.service;

import org.apache.catalina.User;
import org.comcom.dto.AddressDto;
import org.comcom.dto.UserDto;
import org.comcom.dto.UserUpdateRequest;

import java.util.List;

public interface UserService {
    UserDto getUserByEmail(String email);

    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();

    List<UserDto> getFreeInterpreter();

    UserDto updateUser(UserUpdateRequest request);

    byte[] getProfilePhoto(String imageName);

    UserDto updateUserAddress(String email, AddressDto userAddressRequest);

    UserDto changeStatus(String status);

    UserDto changeEmail(String oldEmail, String newEmail);

    UserDto changePassword(String email, String password);

    UserDto addWallet(Integer money);

    boolean setPrice(Integer price);

    boolean deleteUserByEmail(String email, String authUser);
}
