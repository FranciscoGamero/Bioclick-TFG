package com.salesianostriana.bioclick.dto.manager;

import com.salesianostriana.bioclick.model.Manager;

import java.time.LocalDateTime;
import java.util.UUID;

public record ManagerDto(
        UUID id,
        String username,
        String correo,
        String password,
        String fotoPerfilUrl,
        String ultimaAccion,
        LocalDateTime fechaUltimaAccion
) {
    public static ManagerDto of(Manager manager, String urlImagen){
        return new ManagerDto(
                manager.getId(),
                manager.getUsername(),
                manager.getCorreo(),
                manager.getPassword(),
                urlImagen,
                manager.getUltimaAccionProducto(),
                manager.getFechaUltimaAccionProducto()
        );
    }
    public static ManagerDto of(Manager manager){
        return new ManagerDto(
                manager.getId(),
                manager.getUsername(),
                manager.getCorreo(),
                manager.getPassword(),
                manager.getFotoPerfil(),
                manager.getUltimaAccionProducto(),
                manager.getFechaUltimaAccionProducto()
        );
    }
}
