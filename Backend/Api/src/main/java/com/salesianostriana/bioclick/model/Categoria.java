package com.salesianostriana.bioclick.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@SQLDelete(sql = "UPDATE categoria SET borrado = true WHERE id=?")
@FilterDef(name = "categoriaBorradoFiltro", parameters = @ParamDef(name = "isBorrado", type = Boolean.class))
@Filter(name = "categoriaBorradoFiltro", condition = "borrado = :isBorrado")
public class Categoria {

    @Id @GeneratedValue
    private UUID id;

    private String nombreCategoria;

    private boolean borrado = false;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_padre")
    private Set<Categoria> subcategorias;

    @ManyToOne
    @JoinColumn(name = "categoria_padre", insertable = false, updatable = false)
    private Categoria categoriaPadre;

    @ManyToMany(mappedBy = "categorias", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Producto> listaProductos = new HashSet<>();
}
