package com.salesianostriana.bioclick.dto.estadisticas;

import com.salesianostriana.bioclick.dto.Categoria.CategoriaDto;

public record CategoriasMasValoradasDto(
        CategoriaDto categoria,
        Double mediaPuntuacion
) {
}
