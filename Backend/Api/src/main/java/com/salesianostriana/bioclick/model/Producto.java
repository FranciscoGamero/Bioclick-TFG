package com.salesianostriana.bioclick.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@SQLDelete(sql = "UPDATE producto SET borrado = true WHERE id=?")
@FilterDef(name = "productoBorradoFiltro", parameters = @ParamDef(name = "isBorrado", type = Boolean.class))
@Filter(name = "productoBorradoFiltro", condition = "borrado = :isBorrado")
public class Producto {

    @Id
    @GeneratedValue
    private UUID id;
    private String nombreProducto;
    private String descripcion;
    private String imagenProducto;
    private double precioProducto;
    private String fabricante;

    private boolean borrado = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "id_creador", referencedColumnName = "id")
    private User creadoPor;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @OneToOne
    private ImpactoAmbiental impactoAmbiental;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "producto_categoria",
            joinColumns = @JoinColumn(name = "producto_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    @Builder.Default
    private Set<Categoria> categorias = new HashSet<>();

    public static Specification<Producto> precioMenorQue(double max) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.lessThanOrEqualTo(root.get("precioProducto"), max);
        };
    }


    public static Specification<Producto> precioMayorQue(double min) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.greaterThanOrEqualTo(root.get("precioProducto"), min);
        };
    }

    public static Specification<Producto> nombreContiene(String nombre) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("nombreProducto"), "%" + nombre + "%");
        };
    }

    public void addCreador(Admin creador){
        this.creadoPor = creador;
        creador.getProductosCreados().add(this);
    }
    public void removeCreador(Admin creador){
        this.creadoPor = creador;
        creador.getProductosCreados().remove(this);
    }

    public void addCreador(Manager creador){
        this.creadoPor = creador;
        creador.getProductosCreados().add(this);
    }
    public void removeCreador(Manager creador){
        this.creadoPor = creador;
        creador.getProductosCreados().remove(this);
    }

    public void addCategoria(Categoria categoria) {
        this.categorias.add(categoria);
        categoria.getListaProductos().add(this);
    }

    public void removeCategoria(Categoria categoria) {
        this.categorias.remove(categoria);
        categoria.getListaProductos().remove(this);
    }
}
