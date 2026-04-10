package com.aejimenezdev.authService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Email es requerido")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Password es requerido")
    @Size(min = 6, message = "Password debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "Full name es requerido")
    private String fullName;
}