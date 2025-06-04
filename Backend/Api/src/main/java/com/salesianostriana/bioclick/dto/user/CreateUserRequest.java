package com.salesianostriana.bioclick.dto.user;

import com.salesianostriana.bioclick.validation.UsernameUnico;
import com.salesianostriana.bioclick.validation.ValoresCamposCoincidientes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@ValoresCamposCoincidientes(
        field = "password",
        fieldMatch = "verifyPassword",
        message = "Los valores de password y verifyPassword no coinciden")
public record CreateUserRequest(
        @UsernameUnico
        String username,
        @NotEmpty(message = "{createUserRequest.password.notempty}")
        @NotBlank(message = "{createUserRequest.password.notblank}")
        String password,
        @NotEmpty(message = "{createUserRequest.verifyPassword.notempty}")
        @NotBlank(message = "{createUserRequest.verifyPassword.notblank}")
        String verifyPassword,
        @NotNull(message = "{createUserRequest.correo.notnull}")
        @NotBlank(message = "{createUserRequest.correo.notblank}")
        @NotEmpty(message = "{createUserRequest.correo.notempty}")
        String correo) {}
