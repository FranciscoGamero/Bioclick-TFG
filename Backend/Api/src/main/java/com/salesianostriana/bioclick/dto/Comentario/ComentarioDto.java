package com.salesianostriana.bioclick.dto.Comentario;

import com.salesianostriana.bioclick.model.Comentario;

import java.util.UUID;

public record ComentarioDto(
        UUID id,
        String comentario,
        UUID usuarioId,
        UUID productoId
) {
    public static ComentarioDto of(Comentario comentario){
        return new ComentarioDto(
                comentario.getIdComentario(),
                comentario.getComentario(),
                comentario.getUsuario().getId(),
                comentario.getProducto().getId()
        );
    }
}
