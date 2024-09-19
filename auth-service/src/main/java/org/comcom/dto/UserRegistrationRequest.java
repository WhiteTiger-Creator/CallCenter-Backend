package org.comcom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class UserRegistrationRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank(message = "Gender can not be empty")
    @Pattern(regexp = "^[MFO]$", message = "Invalid gender value. Valid values are 'M' for Male, 'F' for Female, and 'O' for Other.")
    private String gender;

    @NotBlank
    private String email;

    @NotNull
    private LocalDate dob;

    @NotBlank
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String phone;

    @NotBlank
    private String profilePhoto;

    private AddressDto addressDto;


}
