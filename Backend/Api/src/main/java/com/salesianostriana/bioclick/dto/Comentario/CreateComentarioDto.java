package com.salesianostriana.bioclick.dto.Comentario;

import java.util.UUID;

public record CreateComentarioDto(
        String comentario,
        UUID userId,
        UUID productoId) {
}
