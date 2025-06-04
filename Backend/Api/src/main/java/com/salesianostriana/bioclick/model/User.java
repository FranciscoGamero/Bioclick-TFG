package com.salesianostriana.bioclick.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="user_entity")
@Inheritance(strategy = InheritanceType.JOINED)

@SQLDelete(sql = "UPDATE user_entity SET borrado = true WHERE id=?")
@FilterDef(name = "userBorradoFiltro", parameters = @ParamDef(name = "isBorrado", type = Boolean.class))
@Filter(name = "userBorradoFiltro", condition = "borrado = :isBorrado")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String correo;

    private String password;

    private String fotoPerfil;

    private LocalDateTime fechaRegistro;

    @Builder.Default
    private Boolean enabled = false;

    private String verificationCode;

    @Builder.Default
    private String role = "ROLE_USUARIO";

    private boolean borrado = false;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario", orphanRemoval = true)
    @Builder.Default
    private Set<Valoracion> listaValoraciones = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario", orphanRemoval = true)
    @Builder.Default
    private Set<Favorito> listaFavoritos = new HashSet<>();

    public void addFavorito(Favorito f){
        this.getListaFavoritos().add(f);
        f.setUsuario(this);

    }
    public void removeFavorito(Favorito f){
        this.getListaFavoritos().remove(f);
        f.setUsuario(null);
    }
    public void addValoracion(Valoracion v){
        this.getListaValoraciones().add(v);
        v.setUsuario(this);
    }
    public void removeValoracion(Valoracion v){
        this.getListaValoraciones().remove(v);
        v.setUsuario(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    public static Specification<User> nombreContiene(String nombreUsuario) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("username"), "%" + nombreUsuario + "%");
        };
    }

    public static Specification<User> correoContiene(String email) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("correo"), "%" + email + "%");
        };
    }

    public static Specification<User> registroMayorQue(LocalDateTime min) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.greaterThanOrEqualTo(root.get("fechaRegistro"), min);
        };
    }
    public static Specification<User> registroMenorQue(LocalDateTime max) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.lessThanOrEqualTo(root.get("fechaRegistro"), max);
        };
    }

}
