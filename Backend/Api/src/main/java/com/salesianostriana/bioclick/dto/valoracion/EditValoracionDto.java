package com.salesianostriana.bioclick.dto.valoracion;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EditValoracionDto (
        @NotNull(message = "{editValoracionDto.comentario.notnull}")
        String comentario,
        @Min(0)
        @Max(10)
        Double puntuacion
){
}
