package com.salesianostriana.bioclick.repository;

import com.salesianostriana.bioclick.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ManagerRepository extends JpaRepository<Manager, UUID> {
}
