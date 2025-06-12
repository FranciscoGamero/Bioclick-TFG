package com.salesianostriana.bioclick.dto.Comentario;

import com.salesianostriana.bioclick.model.Comentario;

import java.time.LocalDateTime;
import java.util.UUID;

public record ComentarioDto(
        UUID id,
        String comentario,
        UUID usuarioId,
        String username,
        String fotoPerfil,
        UUID productoId,
        LocalDateTime fechaComentario) {
    public static ComentarioDto of(Comentario comentario){
        return new ComentarioDto(
                comentario.getIdComentario(),
                comentario.getComentario(),
                comentario.getUsuario().getId(),
                comentario.getUsuario().getUsername(),
                comentario.getUsuario().getFotoPerfil(),
                comentario.getProducto().getId(),
                comentario.getFechaComentario()
        );
    }
}
