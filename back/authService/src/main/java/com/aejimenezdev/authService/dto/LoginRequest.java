package com.aejimenezdev.authService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email es requerido")
    private String email;

    @NotBlank(message = "Password es requerido")
    private String password;
}