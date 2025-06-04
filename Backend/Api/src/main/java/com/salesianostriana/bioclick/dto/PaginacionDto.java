package com.salesianostriana.bioclick.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PaginacionDto<T>(
        int numPagina,
        int tamanioPagina,
        long elementosEncontrados,
        int paginasTotales,
        List<T> contenido
) {

    public static <T> PaginacionDto<T> of(Page<T> page) {
        return new PaginacionDto<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getContent()
        );
    }
}
