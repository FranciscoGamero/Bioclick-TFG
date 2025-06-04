package com.salesianostriana.bioclick.repository;

import com.salesianostriana.bioclick.model.Favorito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface FavoritoRepository extends JpaRepository<Favorito, UUID> {


    @Query("""
    
    Select f from Favorito f
    where f.usuario.id = :id
    """)
    Page<Favorito> buscarPorId(Pageable pageable, UUID id);
}
