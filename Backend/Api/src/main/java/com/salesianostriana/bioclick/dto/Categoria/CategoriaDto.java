package com.salesianostriana.bioclick.dto.Categoria;

import com.salesianostriana.bioclick.dto.producto.ProductoDto;
import com.salesianostriana.bioclick.model.Categoria;

import java.util.List;
import java.util.UUID;

public record CategoriaDto(
        UUID id,
        String nombreCategoria,
        String nombreCategoriaPadre,
        List<String> nombresSubcategorias,
        List<ProductoDto> listaProductos
) {
    public static CategoriaDto of(Categoria categoria) {
        String nombreCategoriaPadre = (categoria.getCategoriaPadre() != null)
                ? categoria.getCategoriaPadre().getNombreCategoria()
                : "None";

        return new CategoriaDto(
                categoria.getId(),
                categoria.getNombreCategoria(),
                nombreCategoriaPadre,
                categoria.getSubcategorias().stream()
                        .map(Categoria::getNombreCategoria)
                        .toList(),
                categoria.getListaProductos().stream().map(ProductoDto::of).toList());
    }
}
