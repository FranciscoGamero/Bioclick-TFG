package com.salesianostriana.bioclick.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Valoracion {

    @EmbeddedId
    private ValoracionPK valoracionPK = new ValoracionPK();


    //Sobre 10
    private Double puntuacion;

    private String comentario;

    private LocalDateTime fechaValorado;

    @ManyToOne
    @MapsId("usuario_id")
    @JoinColumn(name = "usuario_id")
    private User usuario;

    @ManyToOne
    @MapsId("producto_id")
    @JoinColumn(name = "producto_id")
    private Producto producto;

}
