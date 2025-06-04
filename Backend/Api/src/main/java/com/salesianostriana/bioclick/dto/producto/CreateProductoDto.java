package com.salesianostriana.bioclick.dto.producto;

import com.salesianostriana.bioclick.model.Estado;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public record CreateProductoDto(
        @NotBlank(message = "{createProductoDto.nombreProducto.notblank}")
        String nombreProducto,
        @NotNull
        @NotBlank(message = "{createProductoDto.descripcion.notblank}")
        @NotEmpty
        String descripcion,
        @Min(0)
        double precioProducto,
        @NotNull(message = "{createProductoDto.fabricante.notnull}")
        @NotBlank(message = "{createProductoDto.fabricante.notblank}")
        @NotEmpty(message = "{createProductoDto.fabricante.notempty}")
        String fabricante,
        @NotNull
        Estado estado) {
}
