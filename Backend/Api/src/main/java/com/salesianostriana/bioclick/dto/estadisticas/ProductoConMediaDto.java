package com.salesianostriana.bioclick.dto.estadisticas;

import com.salesianostriana.bioclick.model.Producto;

import java.util.UUID;

public record ProductoConMediaDto(
        UUID id,
        String nombreProducto,
        String descripcion,
        Double precioProducto,
        Double mediaPuntuacion
) {
}
