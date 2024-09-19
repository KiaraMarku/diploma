package com.example.diploma.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentRegistrationDTO {

    @NotEmpty(message = "Username is required")
    String username;

    @NotEmpty(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password;

    @NotEmpty(message = "Confirm Password is required")
    String confirmPassword;

    @NotEmpty(message = "First Name is required")
    String firstName;

    @NotEmpty(message = "Last Name is required")
    String lastName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    String email;

    private String birthDate;

    private String gender;

    @NotEmpty(message = "Card Id is required")
    private String cardId;

    Integer groupId;


}
