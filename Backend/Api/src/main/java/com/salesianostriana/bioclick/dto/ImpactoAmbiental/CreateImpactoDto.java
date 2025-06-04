package com.salesianostriana.bioclick.dto.ImpactoAmbiental;

import com.salesianostriana.bioclick.model.ImpactoAmbiental;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CreateImpactoDto(
        @NotNull(message = "{createImpactoDto.productoId.notnull}")
        @NotBlank(message = "{createImpactoDto.productoId.notblank}")
        @NotEmpty(message = "{createImpactoDto.productoId.notempty}")
        String productoId,
        @Min(0)
        Double reduccionCo2,
        Set<String> ahorroMateriales
) {

}
