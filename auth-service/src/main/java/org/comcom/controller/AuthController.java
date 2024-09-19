package org.comcom.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.comcom.dto.*;
import org.comcom.repository.UserRepository;
import org.comcom.service.AuthService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static org.comcom.utils.ApiResponseUtils.buildResponse;

@CrossOrigin
@RestController
@RequestMapping("/v1")
@AllArgsConstructor
@Slf4j(topic = "AuthController")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping(path = "/uploadPhoto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> uploadPhoto(@RequestParam("file") MultipartFile multipartFile) {
        System.out.println("file://" + System.getProperty("user.dir") + "/auth-service/src/main/upload");
        return buildResponse(authService.uploadPhoto(multipartFile));
    }

    @PostMapping(path = "/{role}/register",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> registration(@PathVariable String role, @RequestBody UserRegistrationRequest userRegistrationRequest) {
        String roleName = role.toUpperCase();
        Roles roleEnum = Roles.valueOf(roleName);
        return buildResponse(authService.registerUser(userRegistrationRequest, roleEnum));
    }

    @PostMapping(path = "/anonymousLogin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> anonymousLogin(){
        return buildResponse(authService.anonymousLogin());
    }

    @PostMapping(path = "/auth",produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> authenticate(@RequestBody @Valid LoginRequest loginRequest) {
        System.out.println(loginRequest);
        return buildResponse(authService.authenticate(loginRequest));
    }

    @PostMapping(path = "/verifyEmail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> verifyEmail(@RequestParam("userEmail") String userEmail, @RequestParam("data") String token){
        return buildResponse(authService.verifyEmail(userEmail,token));
    }

    @PutMapping("/request-password-reset")
    ApiResponse<?> requestPasswordReset(@RequestBody @Valid PasswordResetRequest resetRequest) {
        authService.requestPasswordReset(resetRequest);
        return buildResponse(null);
    }

    @PutMapping("/reset-password")
    ApiResponse<?> resetPassword(@RequestBody @Valid PasswordReset passwordReset) {
        return buildResponse(authService.resetPassword(passwordReset));
    }
}
