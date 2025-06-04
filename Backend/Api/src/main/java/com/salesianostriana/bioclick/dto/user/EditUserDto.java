package com.salesianostriana.bioclick.dto.user;

import com.salesianostriana.bioclick.validation.UsernameUnico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EditUserDto(
        @UsernameUnico
        String username,
        @NotNull(message = "{editUserDto.correo.notnull}")
        @NotBlank(message = "{editUserDto.correo.notblank}")
        @NotEmpty(message = "{editUserDto.correo.notempty}")
        String correo,
        @NotNull(message = "{editUserDto.password.notnull}")
        @NotBlank(message = "{editUserDto.password.notblank}")
        @NotEmpty(message = "{editUserDto.password.notempty}")
        String password
) {
}
