package com.salesianostriana.bioclick.dto.Categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CreateCategoriaDto(
        @NotNull(message = "{createCategoriaDto.nombreCategoria.notnull}")
        @NotBlank(message = "{createCategoriaDto.nombreCategoria.notblank}")
        @NotEmpty(message = "{createCategoriaDto.nombreCategoria.notempty}")
        String nombreCategoria,
        String categoriaPadreId,
        Set<String> subcategoriaIds
) {
}
