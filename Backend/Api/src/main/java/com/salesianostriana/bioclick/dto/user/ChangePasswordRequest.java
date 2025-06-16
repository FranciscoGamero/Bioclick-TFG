package com.salesianostriana.bioclick.dto.user;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword
) {
}
