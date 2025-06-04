package com.salesianostriana.bioclick.model;

import jakarta.persistence.Embeddable;
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
public class FavoritoPK {

    private UUID producto_id;
    private UUID usuario_id;
}
