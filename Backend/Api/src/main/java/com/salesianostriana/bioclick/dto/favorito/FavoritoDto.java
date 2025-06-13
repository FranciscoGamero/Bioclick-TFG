package com.salesianostriana.bioclick.dto.favorito;

import com.salesianostriana.bioclick.model.Favorito;

import java.time.LocalDateTime;
import java.util.UUID;

public record FavoritoDto (
        String nombreUsuario,
        UUID idUsuario,
        String nombreProducto,
        UUID idProducto,
        LocalDateTime fechaFavorito){
    public static FavoritoDto of(Favorito favorito) {
        return new FavoritoDto(
                favorito.getUsuario().getUsername(),
                favorito.getUsuario().getId(),
                favorito.getProducto().getNombreProducto(),
                favorito.getProducto().getId(),
                favorito.getFechaFavorito());
    }
}
