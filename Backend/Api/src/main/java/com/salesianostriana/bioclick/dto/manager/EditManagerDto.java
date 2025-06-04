package com.salesianostriana.bioclick.dto.manager;

import com.salesianostriana.bioclick.validation.UsernameUnico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EditManagerDto(
        @UsernameUnico
        String username,
        @NotNull(message = "{editManagerDto.correo.notnull}")
        @NotBlank(message = "{editManagerDto.correo.notblank}")
        @NotEmpty(message = "{editManagerDto.correo.notempty}")
        String correo,
        @NotNull(message = "{editManagerDto.password.notnull}")
        @NotBlank(message = "{editManagerDto.password.notblank}")
        @NotEmpty(message = "{editManagerDto.password.notempty}")
        String password

) {
}
