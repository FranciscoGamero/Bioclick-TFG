package com.salesianostriana.bioclick.repository;

import com.salesianostriana.bioclick.model.ImpactoAmbiental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ImpactoAmbientalRepository extends JpaRepository<ImpactoAmbiental, UUID> {

    @Query("""
            select i from ImpactoAmbiental i
            left join fetch i.ahorroMateriales
            """)
    Page<ImpactoAmbiental> buscarTodos(Pageable pageable);

    @Query("""
            select i from ImpactoAmbiental i
            left join fetch i.ahorroMateriales
            where i.id = :impactoId
            """)
    ImpactoAmbiental buscarUnImpacto(UUID impactoId);
}
