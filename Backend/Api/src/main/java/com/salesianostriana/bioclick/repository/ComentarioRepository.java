package com.salesianostriana.bioclick.repository;

import com.salesianostriana.bioclick.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ComentarioRepository extends JpaRepository<Comentario, UUID> {
}
