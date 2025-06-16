package com.salesianostriana.bioclick.dto.valoracion;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EditValoracionDto (
        Double puntuacion
){
}
