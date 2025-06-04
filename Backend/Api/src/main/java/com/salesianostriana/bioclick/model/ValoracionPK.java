package com.salesianostriana.bioclick.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
@Embeddable
@AllArgsConstructor
public class ValoracionPK {

    @GeneratedValue
    private UUID id_generado;
    private UUID producto_id;
    private UUID usuario_id;
}
