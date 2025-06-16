package com.salesianostriana.bioclick.dto.producto;

import com.salesianostriana.bioclick.model.Estado;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public record EditProductoDto(
        @NotBlank(message = "{createProductoDto.nombreProducto.notblank}")
        String nombreProducto,
        @NotNull
        @NotBlank(message = "{createProductoDto.descripcion.notblank}")
        @NotEmpty
        String descripcion,
        @Min(0)
        double precioProducto,
        @NotNull
        Estado estado,
        String idCategoria) {
}
