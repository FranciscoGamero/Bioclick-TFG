package com.salesianostriana.bioclick.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianostriana.bioclick.model.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        UserDto usuario,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String token,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String refreshToken,
        Boolean enabled) {

    public static UserResponse of (User user, String token, String refreshToken) {
        return new UserResponse(user.getId(), UserDto.of(user), token, refreshToken, user.getEnabled());
    }

}
