package com.salesianostriana.bioclick.repository;

import com.salesianostriana.bioclick.dto.Categoria.CategoriaDto;
import com.salesianostriana.bioclick.model.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {

    @Query("""
        select c from Categoria c
        left join fetch c.subcategorias
        """)
    Page<Categoria> buscarCategorias(Pageable pageable);

    @Query("SELECT c FROM Categoria c LEFT JOIN FETCH c.subcategorias WHERE c.id = :id")
    Optional<Categoria> findByIdWithSubcategorias(UUID id);

    @Modifying
    @Query("""
    delete from Producto p
    where :id in (select c.id from p.categorias c)
""")
    void eliminarProductosPorCategoria(UUID id);
}
