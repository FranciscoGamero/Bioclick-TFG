package com.salesianostriana.bioclick.dto.manager;

import com.salesianostriana.bioclick.validation.UsernameUnico;
import com.salesianostriana.bioclick.validation.ValoresCamposCoincidientes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@ValoresCamposCoincidientes(
        field = "password",
        fieldMatch = "verifyPassword",
        message = "Los valores de password y verifyPassword no coinciden")

public record CreateManagerDto (
        @UsernameUnico
        String username,
        @NotEmpty(message = "{createManagerDto.password.notempty}")
        @NotBlank(message = "{createManagerDto.password.notblank}")
        String password,
        @NotEmpty(message = "{createManagerDto.verifyPassword.notempty}")
        @NotBlank(message = "{createManagerDto.verifyPassword.notblank}")
        String verifyPassword,
        @NotNull(message = "{createManagerDto.correo.notnull}")
        @NotBlank(message = "{createManagerDto.correo.notblank}")
        @NotEmpty(message = "{createManagerDto.correo.notempty}")
        String correo
){
}
