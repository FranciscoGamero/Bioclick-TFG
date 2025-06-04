package com.salesianostriana.bioclick.repository;

import com.salesianostriana.bioclick.model.Valoracion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ValoracionRepository extends JpaRepository<Valoracion, UUID> {

    @Query("""
        
        select v from Valoracion v
        where v.producto.id = :idProducto
        """)
    Page<Valoracion> buscarValoracionesDeProducto(UUID idProducto, Pageable pageable);

    @Query("""
        select v from Valoracion v
        Where v.usuario.id = :userid
        """)
    Page<Valoracion> buscarValoracionesUsuario(Pageable pageable, UUID userid);


    @Query("""
        select v from Valoracion v
        where v.valoracionPK.id_generado = :valoracionId
        """)
    Optional<Valoracion> buscarPorIdAutogenerado(UUID valoracionId);
}
