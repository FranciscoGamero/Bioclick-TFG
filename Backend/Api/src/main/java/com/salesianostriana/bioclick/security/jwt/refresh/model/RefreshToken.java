package com.salesianostriana.bioclick.security.jwt.refresh.model;

import com.salesianostriana.bioclick.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private User user;


    @Column(nullable = false)
    private Instant expireAt;

    @Builder.Default
    private Instant createdAt = Instant.now();

    public String getToken() {
        return id.toString();
    }

}
