package com.salesianostriana.bioclick.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Favorito {


    @EmbeddedId
    private FavoritoPK favoritoPK = new FavoritoPK();

    private LocalDateTime fechaFavorito;

    @ManyToOne
    @MapsId("usuario_id")
    @JoinColumn(name = "usuario_id")
    private User usuario;

    @ManyToOne
    @MapsId("producto_id")
    @JoinColumn(name = "producto_id")
    private Producto producto;


}
