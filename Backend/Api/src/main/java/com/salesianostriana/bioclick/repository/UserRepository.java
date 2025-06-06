package com.salesianostriana.bioclick.repository;

import com.salesianostriana.bioclick.model.ImpactoAmbiental;
import com.salesianostriana.bioclick.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findFirstByUsername(String username);

    Optional<User> findFirstByVerificationCode(String verificationCode);

    @Query("""
            select u from User u
            where u.role = 'ROLE_USUARIO'
            """)
    Page<User> buscarSoloUser(Pageable pageable);

    boolean existsByUsername(String s);

}
