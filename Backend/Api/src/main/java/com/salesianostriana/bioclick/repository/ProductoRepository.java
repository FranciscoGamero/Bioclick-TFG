package com.salesianostriana.bioclick.repository;

import com.salesianostriana.bioclick.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ProductoRepository extends JpaRepository<Producto, UUID>, JpaSpecificationExecutor<Producto> {

    @Query("""
            select p from Producto p
           """)
    Page<Producto> buscarProductos(Pageable pageable);

    @Modifying
    @Query("""
        delete from Valoracion v
        where v.producto.id = :id
       """)
    void borrarValoracionesProducto(UUID id);

    @Modifying
    @Query("""
        delete from Favorito f
        where f.producto.id = :id
       """)
    void borrarFavoritosProducto(UUID id);

    @Modifying
    @Query("""
        delete from ImpactoAmbiental i
        where i.producto.id = :id
       """)
    void borrarImpactoAmbientalProducto(UUID id);

    @Query("""
    SELECT f.producto
    FROM Favorito f
    GROUP BY f.producto
    ORDER BY COUNT(f.producto) DESC
""")
    Page<Producto> productosMasLikeados(Pageable pageable);
    @Query("""
            select v.producto
            from Valoracion v
            group by v.producto
            order by avg(v.puntuacion) desc
""")
    Page<Producto> productosMejorValorados(Pageable pageable);

    @Query("SELECT p FROM Producto p JOIN p.categorias c WHERE c.nombreCategoria = :nombreCategoria")
    Page<Producto> findByNombreCategoria(String nombreCategoria, Pageable pageable);
}

