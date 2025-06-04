package com.salesianostriana.bioclick.security.jwt.refresh.repository;

import com.salesianostriana.bioclick.model.User;
import com.salesianostriana.bioclick.security.jwt.refresh.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, UUID> {


    @Modifying
    @Transactional
    void deleteByUser(User user);

}