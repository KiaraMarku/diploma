package com.example.diploma.dto;

import lombok.Data;

@Data
public class PasswordChangeDTO {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    public boolean isValid() {
        return newPassword.equals(confirmPassword);
    }
}