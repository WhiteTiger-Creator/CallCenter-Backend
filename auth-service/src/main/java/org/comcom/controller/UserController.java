package org.comcom.controller;

import lombok.AllArgsConstructor;
import org.comcom.config.security.SecurityUtils;
import org.comcom.dto.AddressDto;
import org.comcom.dto.ApiResponse;
import org.comcom.dto.UserUpdateRequest;
import org.comcom.model.Users;
import org.comcom.repository.UserRepository;
import org.comcom.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.comcom.utils.ApiResponseUtils.buildResponse;

@CrossOrigin
@RestController
@RequestMapping("/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping(path = "/addWallet", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> addWallet(@RequestBody Integer money){
        System.out.println(money);
        return buildResponse(userService.addWallet(money));
    }
    @GetMapping(path = "/getProfilePhoto/{imageName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> getProfilePhoto(@PathVariable String imageName){
        byte[] image = userService.getProfilePhoto(imageName);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> fetchAllUsers() {
        return buildResponse(userService.getAllUsers());
    }

    @GetMapping(path = "/current-user", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> fetchCurrentUser(@RequestHeader("x-auth-user") String authUser) {
        return buildResponse(userService.getUserByEmail(authUser));
    }

    @GetMapping(path = "/freeInterpreter", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> getFreeInterpreters(){ return buildResponse(userService.getFreeInterpreter());}

    @GetMapping(path = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> fetchUserByEmail(@PathVariable String email) {
        return buildResponse(userService.getUserByEmail(email));
    }

    @GetMapping(path = "/userid/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> fetchUserById(@PathVariable Long id) {
        return buildResponse(userService.getUserById(id));
    }


    @GetMapping(path = "/check-status", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> checkUserStatus(@RequestHeader("x-auth-user") String authUser) {
        return buildResponse(userService.getUserByEmail(authUser).getStatus());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> updateUser(@RequestBody @Valid UserUpdateRequest request) {
        return buildResponse(userService.updateUser(request));
    }

    @PutMapping(path = "/setPrice", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> setPrice(@RequestParam("price") Integer price){
        return buildResponse(userService.setPrice(price));
    }

    @PutMapping(path = "/changeStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> changeStatus(@RequestHeader("status") String status) {
        return buildResponse(userService.changeStatus(status));
    }

    @PutMapping(path = "/changeEmail", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> changeEmail(@RequestParam("oldEmail") String oldEmail, @RequestParam("newEmail") String newEmail) {
        System.out.println("oldEmail: " + oldEmail);
        System.out.println("newEmail: " + newEmail);
        return buildResponse(userService.changeEmail(oldEmail, newEmail));
    }

    @PutMapping(path = "/changePassword", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> changePassword(@RequestParam("Email") String email, @RequestParam("newPassword") String password) {
        return buildResponse(userService.changePassword(email, password));
    }


    @PutMapping(path = "/changeAddress", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> updateAddress(@RequestParam("auth")String email, @RequestBody @Valid AddressDto addressDto) {
        return buildResponse(userService.updateUserAddress(email, addressDto));
    }

    @DeleteMapping(path = "/{email}",  produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> deleteUser(@PathVariable String email, @RequestHeader("x-auth-user") String authUser) {
        return buildResponse(userService.deleteUserByEmail(email, authUser));
    }
}
