package com.aeropuerto.auth.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private Integer userId;
    private String newPassword;
}