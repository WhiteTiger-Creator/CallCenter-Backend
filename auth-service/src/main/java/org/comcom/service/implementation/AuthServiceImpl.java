package org.comcom.service.implementation;

import lombok.AllArgsConstructor;
import org.comcom.config.JwtSigner;
import org.comcom.config.PasswordResetProperties;
import org.comcom.config.security.DefaultUserDetails;
import org.comcom.config.security.SecurityUtils;
import org.comcom.dto.*;
import org.comcom.exception.BadInputException;
import org.comcom.exception.IncorrectCredentialsException;
import org.comcom.exception.VerifyNotException;
import org.comcom.exception.WrongResetKeyException;
import org.comcom.model.*;
import org.comcom.notification.NotificationService;
import org.comcom.repository.CompanyRepository;
import org.comcom.repository.RoleRepository;
import org.comcom.repository.SettingRepository;
import org.comcom.repository.UserRepository;
import org.comcom.service.AuthService;
import org.comcom.service.FileStorageService;
import org.comcom.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private UserService userService;
    private UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtSigner jwtSigner;
    private PasswordResetProperties passwordResetProperties;
    private Clock clock;
    private NotificationService notificationService;
    private JavaMailSender javaMailSender;
    private FileStorageService fileStorageService;
    private SettingRepository settingRepository;


    @Override
    public String uploadPhoto(MultipartFile file){

        return fileStorageService.storeFile(file);
    }


    @Override
    @Transactional
    public String registerUser(UserRegistrationRequest user, Roles role) {
        Optional<Users> userOptional = userRepository.findByEmailIgnoreCase(user.getEmail());
        if (userOptional.isPresent()) {
            throw new BadInputException("Mail is already in use.","Mail is already in use.");
        } else {
            Users userEntity = new Users();

            switch (role) {
                case CUSTOMER, COMPANY_LEADER, SWITCHING_CENTER_LEADER, COMMUNICATION_ASSISTANCE, CAPTIONER -> {
                    this.validateCommonAttributes(user);
                }
                case INTERPRETER -> {
                    this.validateCommonAttributes(user);
                    userEntity.setPrice(20);
                }
                case COMPANY_EMPLOYEE, SWITCHING_CENTER_EMPLOYEE-> {
                    this.validateCommonAttributes(user);
                    userEntity.setCompany(companyRepository.findById(userRepository.findByEmailIgnoreCase(SecurityUtils.getAuthentication().getPrincipal().toString()).get().getId()).get());
                }
                case ADMIN -> {
                    userEntity.setVerified(true);
                    userEntity.setSmsVerified(true);
                    userEntity.setStatus(OnlineStatus.ONLINE);
                }
                default -> throw new BadInputException("Invalid Profile", "Invalid Profile");
            }

            Path filePath = Paths.get("./auth-service/src/main/upload", user.getProfilePhoto());
            userEntity.setProfilePhoto(filePath.toString());
            BeanUtils.copyProperties(user, userEntity);
            BeanUtils.copyProperties(user.getAddressDto(), userEntity);
            String password = passwordEncoder.encode(user.getPassword());
            userEntity.setPassword(password);
            userEntity.setProfile(roleRepository.findByName(role.name()));
            userEntity.setWallet(100);

            switch (user.getGender()) {
                case "M" -> userEntity.setGender(Gender.MALE);
                case "F" -> userEntity.setGender(Gender.FEMALE);
                case "O" -> userEntity.setGender(Gender.OTHER);
            }

            userEntity.setStatus(OnlineStatus.ONLINE);

            userEntity.setCreatedOn(LocalDateTime.now());
            userEntity.setUpdatedOn(LocalDateTime.now());
            userEntity.setVerified(false);
            String token = generateVerificationCode();
            System.out.println(token);
            userEntity.setVerificationCode(token);
            userEntity.setVerificationExpiryDate(LocalDateTime.now().plusHours(24));


            if (role != Roles.INTERPRETER) {
//                sendVerificationEmail(userEntity, token);
            }
            else {
                Setting setting = new Setting();
                setting.setUserName(user.getUsername());
                userEntity.setSettings(setting);
                settingRepository.save(setting);
                userEntity.setVerified(true);
            }
            userRepository.save(userEntity);

            String accessToken = jwtSigner.createAccessToken(userEntity.getUsername(), userEntity.getProfile().getName());

            return accessToken;
        }

    }

    @Override
    @Transactional
    public boolean verifyEmail(String userEmail, String token) {
        Optional<Users> userOptional = userRepository.findByEmailIgnoreCase(userEmail);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            if(user.getVerificationCode().equals(token)) {

                if(user.getVerificationExpiryDate().isAfter(LocalDateTime.now())){
                    Setting setting = new Setting();
                    setting.setUserName(user.getUsername());
                    user.setSettings(setting);
                    settingRepository.save(setting);
                    user.setVerified(true);
                    userRepository.save(user);
                    return true;
                } else {
                    throw new BadInputException("Token has expired", "Token has expired");
                }
            }else {
                throw new BadInputException("Invalid token.", "Invalid token.");
            }
        } else {
            throw new BadInputException("User not found for email: " + userEmail,"User not found for email: " + userEmail);
        }
    }

    public  String generateVerificationCode() {
        // Define the possible digits
        String digits = "0123456789";

        // Create a random object
        Random random = new Random();

        // Generate a random 6-digit code
        StringBuilder verificationCode = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            verificationCode.append(digits.charAt(random.nextInt(digits.length())));
        }

        return verificationCode.toString();
    }


    protected void validateCommonAttributes(UserRegistrationRequest userDto) {
        // Validate that the user's first name is not empty
        if (userDto.getFirstName() == null || userDto.getFirstName().isEmpty()) {
            throw new BadInputException("First name cannot be empty", "This field is required");
        }
        // Validate that the user's last name is not empty
        if (userDto.getLastName() == null || userDto.getLastName().isEmpty()) {
            throw new BadInputException("Last name cannot be empty", "This field is required");
        }

        // Validate that the user's email address is not empty
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            throw new BadInputException("Email address cannot be empty", "This field is required");
        }

        // Validate that the user's password is not empty
        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            throw new BadInputException("Password cannot be empty", "This field is required");
        }
    }

    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        try {System.out.println("1111");
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.pwd()));
            System.out.println("sdfsdfsdfsdf");
            Users user = userRepository.findByEmailIgnoreCase(loginRequest.email()).get();
            System.out.println("LoginRequest: " + loginRequest);
            if(user.getVerified()) return signToken(authentication);
            else throw new VerifyNotException();
        } catch (AuthenticationException exception) {
            throw new IncorrectCredentialsException();
        }
    }

    @Override
    public LoginResponse anonymousLogin(){

        byte[] info = null;
        Users user = new Users();
        user.setFirstName("Anonymous");
        user.setLastName("Anonymous");
        System.out.println(user.getId());
        user.setEmail("anonymous"+getAllUserCount()+"@outlook.com");
        user.setPhone("123456789");
        user.setUsername(user.getEmail());
        String pw = passwordEncoder.encode("Anonymous");
        user.setPassword(pw);
        user.setProfile(roleRepository.findByName("Anonymous"));
        userRepository.save(user);
        String accessToken = jwtSigner.createAccessToken(user.getUsername(), user.getProfile().getName());
        String refreshToken = jwtSigner.createRefreshToken(user.getUsername(), user.getProfile().getName());

        return new LoginResponse(accessToken, refreshToken, userRepository.save(user).toDto(), info);
    }

    @Override
    public void requestPasswordReset(PasswordResetRequest resetRequest) {
        userRepository.findByEmailIgnoreCase(resetRequest.email())
            .ifPresent(userEntity -> {
                if (userEntity.getVerified()) {
                    int expirationTimeInHours = passwordResetProperties.expirationTimeInHours();
                    int resetKey = getPasswordResetKey();
                    userEntity.setResetKey(resetKey);
                    userEntity.setResetDate(clock.instant());
                    userRepository.save(userEntity);
//                    notificationService.sendPasswordResetRequestEmail(
//                        userEntity.getEmail(),
//                        userEntity.getFirstName(),
//                        userEntity.getResetKey(),
//                        expirationTimeInHours
//                    );
                }
            });
    }

    @Override
    public LoginResponse resetPassword(PasswordReset passwordReset) {
        byte[] info = null;
        Users userEntity = userRepository.findByResetKey(passwordReset.resetKey())
            .orElseThrow(() -> new WrongResetKeyException(passwordReset.resetKey()));

        int expirationTimeInHours = passwordResetProperties.expirationTimeInHours();
        if (userEntity.getResetDate().plus(expirationTimeInHours, ChronoUnit.HOURS).isAfter(clock.instant())) {
            userEntity.setPassword(passwordEncoder.encode(passwordReset.newPassword()));
            String accessToken = jwtSigner.createAccessToken(userEntity.getUsername(), userEntity.getProfile().getName());
            String refreshToken = jwtSigner.createRefreshToken(userEntity.getUsername(), userEntity.getProfile().getName());
            userEntity.setResetKey(null);
            userEntity.setResetDate(null);
            userRepository.save(userEntity);
            UserDto user = userService.getUserByEmail(userEntity.getUsername());
            updateStatus(user.getEmail());
            user.setStatus(OnlineStatus.ONLINE);

            return new LoginResponse(accessToken, refreshToken, user, info);
        } else {
            throw new WrongResetKeyException(passwordReset.resetKey());
        }
    }

    private LoginResponse signToken(Authentication authentication) {
        byte[] info = null;
        DefaultUserDetails authUser = (DefaultUserDetails) authentication.getPrincipal();
        String accessToken = jwtSigner.createAccessToken(authUser.getUsername(), authUser.getProfile().getName());
        String refreshToken = jwtSigner.createRefreshToken(authUser.getUsername(), authUser.getProfile().getName());
        System.out.println(accessToken.length());
        UserDto userDto = userService.getUserByEmail(authUser.getUsername());
        Users user = userRepository.findByEmailIgnoreCase(userDto.getEmail()).get();
        userRepository.save(user);
        updateStatus(userDto.getEmail());
        userDto.setStatus(OnlineStatus.ONLINE);

        return new LoginResponse(accessToken, refreshToken, userDto, info);
    }

    private void updateStatus(String email){
        Users u = userRepository.findByEmailIgnoreCase(email).get();
        u.setStatus(OnlineStatus.ONLINE);
        userRepository.save(u);
    }

    private int getPasswordResetKey() {
        int resetKey;
        do {
            resetKey = new Random().nextInt(100000, 999999);
        } while (userRepository.findByResetKey(resetKey).isPresent());
        return resetKey;
    }

    private int getAllUserCount(){
        return userRepository.findAll().size();
    }

//    private void sendVerificationEmail(Users user, String token){
//        String subject = "Email Verification";
//        String message = "Email verification code: " + token + " for app.comcom.at";
//
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setFrom("earl0425@outlook.com");
//        mailMessage.setTo(user.getEmail());
//        mailMessage.setSubject(subject);
//        mailMessage.setText(message);
//        javaMailSender.send(mailMessage);
//        System.out.println("your email: " + user.getEmail());
//        System.out.println("Email sent: " + message);
//    }
}
