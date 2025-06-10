package com.salesianostriana.bioclick.dto.estadisticas;


import java.util.UUID;

public record UsuarioConMasValoracionesDto(
        UUID id,
        String username,
        String role,
        Long valoracionesTotales
) {
}
