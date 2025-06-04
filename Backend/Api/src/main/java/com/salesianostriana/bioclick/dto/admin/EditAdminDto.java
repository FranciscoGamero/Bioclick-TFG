package com.salesianostriana.bioclick.dto.admin;

import com.salesianostriana.bioclick.validation.UsernameUnico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EditAdminDto(
        @UsernameUnico
        String username,
        @NotNull(message = "{editAdminDto.correo.notnull}")
        @NotBlank(message = "{editAdminDto.correo.notblank}")
        @NotEmpty(message = "{editAdminDto.correo.notempty}")
        String correo,
        @NotNull(message = "{editAdminDto.password.notnull}")
        @NotBlank(message = "{editAdminDto.password.notblank}")
        @NotEmpty(message = "{editAdminDto.password.notempty}")
        String password

) {
}
