package org.comcom.service.implementation;

import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.comcom.config.security.SecurityUtils;
import org.comcom.dto.AddressDto;
import org.comcom.dto.UserDto;
import org.comcom.dto.UserUpdateRequest;
import org.comcom.exception.ForbiddenException;
import org.comcom.exception.IncorrectCredentialsException;
import org.comcom.exception.NotFoundException;
import org.comcom.model.Gender;
import org.comcom.model.OnlineStatus;
import org.comcom.model.Users;
import org.comcom.repository.UserRepository;
import org.comcom.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
            .orElseThrow(IncorrectCredentialsException::new)
            .toDto();
    }

    @Override
    public UserDto getUserById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(IncorrectCredentialsException::new);
        System.out.println(user);
        UserDto userDto = user.toDto();
        System.out.println("userDto   " + userDto);
        if(user.getCompany() != null)
            userDto.setCompany(user.getCompany().toDto());
        return userDto;
    }

    @Override
    public byte[] getProfilePhoto(String imageName){
        byte[] info = null;
        try {
            Path filePath = Paths.get("./auth-service/src/main/upload", imageName);
            File file = new File(filePath.toString());
            info = Files.readAllBytes(file.toPath());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }

    @Override
    public List<UserDto> getAllUsers(){
        Users currentUser = userRepository.findByEmailIgnoreCase(SecurityUtils.getAuthentication().getPrincipal().toString()).get();
        if(currentUser.getProfile().getId() == 10)
            return userRepository.findAll()
                        .stream()
                        .filter(user -> user.getProfile().getId() == 3 || user.getProfile().getId() == 6 || user.getProfile().getId() == 8)
                        .map(Users::toDto)
                        .collect(Collectors.toList());

        if(currentUser.getProfile().getId() == 6)
            return userRepository.findAll()
                    .stream()
                    .filter(user -> user.getProfile().getId() == 5 && user.getCompany().getId() == currentUser.getCompany().getId())
                    .map(Users::toDto)
                    .collect(Collectors.toList());

        if(currentUser.getProfile().getId() == 8)
            return userRepository.findAll()
                    .stream()
                    .filter(user -> user.getProfile().getId() == 7 || (user.getProfile().getId() == 2 && user.getState().equals(currentUser.getState())))
                    .map(Users::toDto)
                    .collect(Collectors.toList());

        if(currentUser.getProfile().getId() == 7)
            return userRepository.findAll()
                    .stream()
                    .filter(user -> user.getProfile().getId() == 2 && user.getState().equals(currentUser.getState()))
                    .map(Users::toDto)
                    .collect(Collectors.toList());

        if(currentUser.getProfile().getId() == 2)
            return userRepository.findAll()
                    .stream()
                    .filter(user -> user.getProfile().getId() == 3)
                    .map(Users::toDto)
                    .collect(Collectors.toList());

        return null;
    }

    @Override
    public List<UserDto> getFreeInterpreter(){
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getProfile().getId() == 3 && user.getStatus() == OnlineStatus.ONLINE)
                .map(Users::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(UserUpdateRequest request) {
        Users userEntity = userRepository.findByEmailIgnoreCase(request.getEmail()).get();
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        userEntity.setUsername(request.getFirstName() + " " + request.getLastName());
        userEntity.setUpdatedOn(LocalDateTime.now());
        userEntity.setPhone(request.getPhoneNumber());
        userEntity.setProfilePhoto(request.getProfilePhoto());
        switch (request.getGender()) {
            case "M" -> userEntity.setGender(Gender.MALE);
            case "F" -> userEntity.setGender(Gender.FEMALE);
            case "O" -> userEntity.setGender(Gender.OTHER);
        }
        return userRepository.save(userEntity).toDto();
    }

    @Override
    public UserDto updateUserAddress(String email, AddressDto userAddressRequest) {
        Users userEntity = userRepository.findByEmailIgnoreCase(email).get();
        userEntity.setHouseNo(userAddressRequest.getHouseNo());
        userEntity.setStreet(userAddressRequest.getStreet());
        userEntity.setLocation(userAddressRequest.getLocation());
        userEntity.setCity(userAddressRequest.getCity());
        userEntity.setState(userAddressRequest.getState());
        userEntity.setCountry(userAddressRequest.getCountry());
        userEntity.setZip(userAddressRequest.getZip());
        
        return userRepository.save(userEntity).toDto();
    }

    @Override
    public UserDto changeStatus(String status){
        Users userEntity = userRepository.findByEmailIgnoreCase(SecurityUtils.getAuthentication().getPrincipal().toString()).get();
        String statusName = status.toUpperCase();
        OnlineStatus onlineStatus = OnlineStatus.valueOf(statusName);
        userEntity.setStatus(onlineStatus);
        return userRepository.save(userEntity).toDto();
    }

    @Override
    public UserDto changeEmail(String oldEmail, String newEmail){
        Users userEntity = userRepository.findByEmailIgnoreCase(oldEmail).get();
        userEntity.setEmail(newEmail);
        return userRepository.save(userEntity).toDto();
    }

    @Override
    public UserDto changePassword(String email, String password){
        Users userEntity = userRepository.findByEmailIgnoreCase(email).get();
        String pass = passwordEncoder.encode(password);
        userEntity.setPassword(pass);
        return userRepository.save(userEntity).toDto();
    }

    @Override
    public boolean setPrice(Integer price){
        Users userEntity = userRepository.findByEmailIgnoreCase(SecurityUtils.getAuthentication().getPrincipal().toString()).get();
        userEntity.setPrice(price);
        userRepository.save(userEntity);
        return true;
    }

    @Override
    public UserDto addWallet(Integer money){
        System.out.println("server: " + money);
        Users userEntity = userRepository.findByEmailIgnoreCase(SecurityUtils.getAuthentication().getPrincipal().toString()).get();
        userEntity.setWallet(userEntity.getWallet() + money);
        return userRepository.save(userEntity).toDto();
    }

    @Override
    public boolean deleteUserByEmail(String email, String authUser) {
        Users userEntity = userRepository.findByEmailIgnoreCase(authUser).get();

        Optional<Users> checkUser = userRepository.findByEmailIgnoreCase(email);

        if(checkUser.isPresent()){
            String role = userEntity.getProfile().getName();

            switch (role.toUpperCase()){
                case "COMPANY_LEADER", "SWITCHING_CENTER_LEADER", "ADMIN" -> {
                   userRepository.deleteById(checkUser.get().getId());
                    return true;
                }
                default -> throw new ForbiddenException("Not Authorised to delete user", "Unauthorized to delete user");
            }
        }else{
            throw new NotFoundException("User not found", "User not found: "+email);
        }

    }
}
