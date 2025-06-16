package com.salesianostriana.bioclick.repository;

import com.salesianostriana.bioclick.model.Comentario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ComentarioRepository extends JpaRepository<Comentario, UUID> {

    Page<Comentario> findByProductoId(UUID productoId, Pageable pageable);
}
