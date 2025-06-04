package com.salesianostriana.bioclick.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name="manager_entity")
@SuperBuilder
@AllArgsConstructor
@SQLDelete(sql = "UPDATE manager_entity SET borrado = true WHERE id=?")
@FilterDef(name = "managerBorradoFiltro", parameters = @ParamDef(name = "isBorrado", type = Boolean.class))
@Filter(name = "managerBorradoFiltro", condition = "borrado = :isBorrado")
public class Manager extends User{


    private String ultimaAccionProducto;
    private LocalDateTime fechaUltimaAccionProducto;

    private boolean borrado = false;


    @OneToMany(mappedBy = "creadoPor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Producto> productosCreados;


    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(super.getRole()));
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Manager admin = (Manager) o;
        return getId() != null && Objects.equals(getId(), admin.getId())
                && getUsername() != null && Objects.equals(getUsername(), admin.getUsername());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getUsername());
    }
}
