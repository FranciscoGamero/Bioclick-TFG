package com.salesianostriana.bioclick.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Comentario {

    @Id @GeneratedValue
    UUID idComentario;

    private LocalDateTime fechaComentario;

    private String comentario;

    @ManyToOne
    private User usuario;

    @ManyToOne
    private Producto producto;


}
