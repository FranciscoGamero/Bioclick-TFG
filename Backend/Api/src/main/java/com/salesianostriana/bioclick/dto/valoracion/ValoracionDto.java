package com.salesianostriana.bioclick.dto.valoracion;

import com.salesianostriana.bioclick.model.Valoracion;

import java.time.LocalDateTime;

public record ValoracionDto(
        String nombreUsuario,
        String nombreProducto,
        Double puntuacion,
        LocalDateTime fechaValorado) {

    public static ValoracionDto of(Valoracion valoracion) {
        return new ValoracionDto(
                valoracion.getUsuario().getUsername()
                ,valoracion.getProducto().getNombreProducto()
                ,valoracion.getPuntuacion()
                ,valoracion.getFechaValorado());
    }
}
