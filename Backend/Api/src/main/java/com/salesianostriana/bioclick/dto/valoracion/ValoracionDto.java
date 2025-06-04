package com.salesianostriana.bioclick.dto.valoracion;

import com.salesianostriana.bioclick.model.Valoracion;

import java.time.LocalDateTime;

public record ValoracionDto(
        String nombreUsuario,
        String nombreProducto,
        String comentario,
        Double puntuacion,
        LocalDateTime fechaValorado) {

    public static ValoracionDto of(Valoracion valoracion) {
        return new ValoracionDto(
                valoracion.getUsuario().getUsername()
                ,valoracion.getProducto().getNombreProducto()
                ,valoracion.getComentario()
                ,valoracion.getPuntuacion()
                ,valoracion.getFechaValorado());
    }
}
