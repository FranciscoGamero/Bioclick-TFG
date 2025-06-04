package com.salesianostriana.bioclick.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor

@SQLDelete(sql = "UPDATE impacto_ambiental SET borrado = true WHERE id=?")
@FilterDef(name = "impactoBorradoFiltro", parameters = @ParamDef(name = "isBorrado", type = Boolean.class))
@Filter(name = "impactoBorradoFiltro", condition = "borrado = :isBorrado")
public class ImpactoAmbiental {

    @Id @GeneratedValue
    private UUID id;

    private Double reduccionCo2;

    @ElementCollection
    private Set<String> ahorroMateriales;

    @OneToOne
    private Producto producto;

    private boolean borrado = Boolean.FALSE;

}
