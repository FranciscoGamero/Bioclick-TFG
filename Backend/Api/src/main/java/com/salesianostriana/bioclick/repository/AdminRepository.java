package com.salesianostriana.bioclick.repository;

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
}
