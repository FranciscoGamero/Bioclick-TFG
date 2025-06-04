package com.salesianostriana.bioclick.dto.manager;

import com.salesianostriana.bioclick.model.Manager;

import java.time.LocalDateTime;

public record ManagerDto(
        String username,
        String correo,
        String fotoPerfilUrl,
        String ultimaAccion,
        LocalDateTime fechaUltimaAccion
) {
    public static ManagerDto of(Manager manager, String urlImagen){
        return new ManagerDto(
                manager.getUsername(),
                manager.getCorreo(),
                urlImagen,
                manager.getUltimaAccionProducto(),
                manager.getFechaUltimaAccionProducto()
        );
    }
    public static ManagerDto of(Manager manager){
        return new ManagerDto(
                manager.getUsername(),
                manager.getCorreo(),
                manager.getFotoPerfil(),
                manager.getUltimaAccionProducto(),
                manager.getFechaUltimaAccionProducto()
        );
    }
}
