package com.salesianostriana.bioclick.dto.valoracion;

import jakarta.validation.constraints.*;

public record CreateValoracionDto(
        @Min(0)
        @Max(10)
        Double puntuacion,
        @NotNull(message = "{createValoracionDto.comentario.notnull}")
        String comentario,
        @NotNull(message = "{createValoracionDto.user_id.notnull}")
        @NotBlank(message = "{createValoracionDto.user_id.notblank}")
        @NotEmpty(message = "{createValoracionDto.user_id.notempty}")
        String user_id,
        @NotNull(message = "{createValoracionDto.producto_id.notnull}")
        @NotBlank(message = "{createValoracionDto.producto_id.notblank}")
        @NotEmpty(message = "{createValoracionDto.producto_id.notempty}")
        String producto_id
) {
}
