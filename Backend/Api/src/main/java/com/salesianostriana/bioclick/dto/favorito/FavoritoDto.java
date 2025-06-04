package com.salesianostriana.bioclick.dto.favorito;

import com.salesianostriana.bioclick.model.Favorito;

import java.time.LocalDateTime;

public record FavoritoDto (
        String nombreUsuario,
        String nombreProducto,
        LocalDateTime fechaFavorito){
    public static FavoritoDto of(Favorito favorito) {
        return new FavoritoDto(
                favorito.getUsuario().getUsername(),
                favorito.getProducto().getNombreProducto(),
                favorito.getFechaFavorito());
    }
}
