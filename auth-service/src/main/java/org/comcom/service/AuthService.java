package org.comcom.service;

import org.comcom.dto.*;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {

    String registerUser(UserRegistrationRequest user, Roles role);

    boolean verifyEmail(String userEmail, String token);

    LoginResponse anonymousLogin();

    LoginResponse authenticate(LoginRequest loginRequest);

    void requestPasswordReset(PasswordResetRequest resetRequest);

    LoginResponse resetPassword(PasswordReset passwordReset);

    String uploadPhoto(MultipartFile file);
}
