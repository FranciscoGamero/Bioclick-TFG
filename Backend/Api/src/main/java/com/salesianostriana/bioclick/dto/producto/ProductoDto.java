package com.salesianostriana.bioclick.dto.producto;

import com.salesianostriana.bioclick.model.Estado;
import com.salesianostriana.bioclick.model.Producto;

import java.util.UUID;

public record ProductoDto(
        UUID id,
        String nombreProducto,
        String descripcion,
        String fabricante,
        String imagenProducto,
        double precioProducto,
        Estado estado) {

    public static ProductoDto of(Producto producto, String imageUrl) {
        return new ProductoDto(
                producto.getId(),
                producto.getNombreProducto(),
                producto.getDescripcion(),
                producto.getFabricante(),
                imageUrl,
                producto.getPrecioProducto(),
                producto.getEstado());
    }

    public static ProductoDto of(Producto producto) {
        return new ProductoDto(
                producto.getId(),
                producto.getNombreProducto(),
                producto.getDescripcion(),
                producto.getFabricante(),
                producto.getImagenProducto(),
                producto.getPrecioProducto(),
                producto.getEstado());
    }
}
