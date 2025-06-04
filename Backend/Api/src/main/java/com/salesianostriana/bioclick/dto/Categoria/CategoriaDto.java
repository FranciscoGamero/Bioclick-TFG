package com.salesianostriana.bioclick.dto.Categoria;

import com.salesianostriana.bioclick.model.Categoria;

import java.util.List;

public record CategoriaDto(
        String nombreCategoria,
        String nombreCategoriaPadre,
        List<String> nombresSubcategorias
) {
    public static CategoriaDto of(Categoria categoria) {
        String nombreCategoriaPadre = (categoria.getCategoriaPadre() != null)
                ? categoria.getCategoriaPadre().getNombreCategoria()
                : "None";

        return new CategoriaDto(
                categoria.getNombreCategoria(),
                nombreCategoriaPadre,
                categoria.getSubcategorias().stream()
                        .map(Categoria::getNombreCategoria)
                        .toList()
        );
    }
}
