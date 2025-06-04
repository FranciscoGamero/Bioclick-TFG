package com.salesianostriana.bioclick.dto.admin;

import com.salesianostriana.bioclick.model.Admin;

import java.time.LocalDateTime;
import java.util.UUID;

public record AdminDto(
        UUID id,
        String username,
        String correo,
        String fotoPerfilUrl,
        String ultimaAccion,
        LocalDateTime fechaUltimaAccion) {


    public static AdminDto of(Admin admin, String urlImagen) {
        return new AdminDto(
                admin.getId(),
                admin.getUsername(),
                admin.getCorreo(),
                urlImagen,
                admin.getUltimaAccion(),
                LocalDateTime.now());
    }
    public static AdminDto of(Admin admin) {
        return new AdminDto(
                admin.getId(),
                admin.getUsername(),
                admin.getCorreo(),
                admin.getFotoPerfil(),
                admin.getUltimaAccion(),
                LocalDateTime.now());
    }
}
