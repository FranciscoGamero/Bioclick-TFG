package com.salesianostriana.bioclick.repository;

import com.salesianostriana.bioclick.dto.estadisticas.CategoriasMasValoradasDto;
import com.salesianostriana.bioclick.dto.estadisticas.ProductoConMediaDto;
import com.salesianostriana.bioclick.dto.estadisticas.UsuarioConMasValoracionesDto;
import com.salesianostriana.bioclick.model.Admin;
import com.salesianostriana.bioclick.model.Manager;
import com.salesianostriana.bioclick.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {



    Optional<Admin> findFirstByVerificationCode(String verificationCode);

    @Query("""
            select a from Admin a
            join User u on a.id = u.id
        """)
    Page<Admin> buscarAdmins(Pageable pageable);

    @Query("""
            select m from Manager m
            join User u on m.id = u.id
        """)
    Page<Manager> buscarManagers(Pageable pageable);

    @Query("""
            SELECT c, AVG(v.puntuacion) as media
            FROM Valoracion v
            JOIN v.producto p
            JOIN p.categorias c
            WHERE p.borrado = false AND c.borrado = false
            GROUP BY c.nombreCategoria
            ORDER BY media DESC
""")
    Page<CategoriasMasValoradasDto> categoriasMasValoradas(Pageable pageable);
    @Query("""
            SELECT c, SUM(i.reduccionCo2)
            FROM Producto p
            JOIN p.categorias c
            JOIN p.impactoAmbiental i
            WHERE p.borrado = false AND c.borrado = false AND i.borrado = false
            GROUP BY c.nombreCategoria
            ORDER BY SUM(i.reduccionCo2) DESC
""")
    Page<CategoriasMasValoradasDto> co2ReducidoPorCategoria(Pageable pageable);

    @Query("""
        select COUNT(p)
        from Producto p
""")
    Long productosTotales();
    @Query("""
        select SUM(i.reduccionCo2)
        from ImpactoAmbiental i
        where i.borrado = false AND i.producto.borrado = false
""")
    double totalCo2();

    @Query("""
    SELECT new com.salesianostriana.bioclick.dto.estadisticas.ProductoConMediaDto(
        v.producto.id,
        v.producto.nombreProducto,
        v.producto.descripcion,
        v.producto.precioProducto,
        AVG(v.puntuacion)
    )
    FROM Valoracion v
    GROUP BY
        v.producto.id,
        v.producto.nombreProducto,
        v.producto.descripcion,
        v.producto.imagenProducto,
        v.producto.precioProducto,
        v.producto.estado
    ORDER BY AVG(v.puntuacion) DESC
""")
    Page<ProductoConMediaDto> topProductosPorMediaPuntuacion(Pageable pageable);

    @Query("SELECT new com.salesianostriana.bioclick.dto.estadisticas.UsuarioConMasValoracionesDto(u.id, u.username, u.role, COUNT(v)) " +
            "FROM User u JOIN u.listaValoraciones v " +
            "GROUP BY u.id, u.username " +
            "ORDER BY COUNT(v) DESC")
    Page<UsuarioConMasValoracionesDto> rankingUsuariosValoradores(Pageable page);


    @Query("""
            select (SUM(case when u.enabled = true then 1 else 0 end) * 100.0) / COUNT(u)
            from User u
""")
    Double porcentajeUsuariosHabilitados();
}
