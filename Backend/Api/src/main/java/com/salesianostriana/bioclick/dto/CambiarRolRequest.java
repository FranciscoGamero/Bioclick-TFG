package com.salesianostriana.bioclick.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CambiarRolRequest (
        @NotNull
        @NotEmpty
        @NotBlank
        String nuevoRol) {
}
