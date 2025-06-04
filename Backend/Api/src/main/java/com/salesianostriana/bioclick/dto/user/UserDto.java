package com.salesianostriana.bioclick.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salesianostriana.bioclick.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String correo,
        String fotoPerfilUrl,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd:HH-mm-ss")
        LocalDateTime fechaRegistro
) {
    public static UserDto of(User user, String fotoPerfil){
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getCorreo(),
                fotoPerfil,
                user.getFechaRegistro());
    }
    public static UserDto of(User user){
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getCorreo(),
                user.getFotoPerfil(),
                user.getFechaRegistro());
    }
}
