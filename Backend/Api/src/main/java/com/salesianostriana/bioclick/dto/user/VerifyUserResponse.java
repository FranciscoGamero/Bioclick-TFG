package com.salesianostriana.bioclick.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianostriana.bioclick.model.User;

import java.util.UUID;

public record VerifyUserResponse(
        UUID id,
        String username,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String token,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String refreshToken,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String verifyToken
) {

    public static VerifyUserResponse of (User user) {
        return new VerifyUserResponse(user.getId(), user.getUsername(), null, null, null);
    }

    public static VerifyUserResponse of (User user, String token, String refreshToken, String verifyToken) {
        return new VerifyUserResponse(user.getId(), user.getUsername(), token, refreshToken, verifyToken);
    }

}
