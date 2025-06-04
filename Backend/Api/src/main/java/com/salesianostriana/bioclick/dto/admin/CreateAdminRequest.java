package com.salesianostriana.bioclick.dto.admin;

import com.salesianostriana.bioclick.validation.UsernameUnico;
import com.salesianostriana.bioclick.validation.ValoresCamposCoincidientes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@ValoresCamposCoincidientes(
        field = "password",
        fieldMatch = "verifyPassword",
        message = "Los valores de password y verifyPassword no coinciden")

public record CreateAdminRequest(
        @UsernameUnico
        String username,
        @NotEmpty(message = "{createAdminRequest.password.notempty}")
        @NotBlank(message = "{createAdminRequest.password.notblank}")
        String password,
        @NotEmpty(message = "{CreateAdminRequest.verifyPassword.notempty}")
        @NotBlank(message = "{createAdminRequest.verifyPassword.notblank}")
        String verifyPassword,
        @NotNull(message = "{createAdminRequest.correo.notnull}")
        @NotBlank(message = "{createAdminRequest.correo.notblank}")
        @NotEmpty(message = "{createAdminRequest.correo.notempty}")
        String correo
) {
}
