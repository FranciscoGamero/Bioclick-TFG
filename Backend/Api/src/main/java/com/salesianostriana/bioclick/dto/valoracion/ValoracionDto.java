package com.salesianostriana.bioclick.dto.valoracion;

import com.salesianostriana.bioclick.model.Valoracion;

import java.time.LocalDateTime;
import java.util.UUID;

public record ValoracionDto(
        UUID valoracionId,
        String nombreUsuario,
        String nombreProducto,
        UUID productoId,
        Double puntuacion,
        LocalDateTime fechaValorado) {

    public static ValoracionDto of(Valoracion valoracion) {
        return new ValoracionDto(
                valoracion.getValoracionPK().getId_generado(),
                valoracion.getUsuario().getUsername(),
                valoracion.getProducto().getNombreProducto(),
                valoracion.getProducto().getId(),
                valoracion.getPuntuacion(),
                valoracion.getFechaValorado());
    }
}
