package com.salesianostriana.bioclick.dto.Categoria;

import com.salesianostriana.bioclick.dto.producto.ProductoDto;
import com.salesianostriana.bioclick.model.Categoria;

import java.util.List;
import java.util.UUID;

public record CategoriaDto(
        UUID id,
        String nombreCategoria,
        String nombreCategoriaPadre,
        UUID idCategoriaPadre,
        List<String> nombresSubcategorias,
        List<UUID> listaIdSubcategorias,
        List<ProductoDto> listaProductos
) {
    public static CategoriaDto of(Categoria categoria) {
        String nombreCategoriaPadre = (categoria.getCategoriaPadre() != null)
                ? categoria.getCategoriaPadre().getNombreCategoria()
                : "None";
        UUID idCategoriaPadre = (categoria.getCategoriaPadre() != null)
                ? categoria.getCategoriaPadre().getId()
                : null;
        return new CategoriaDto(
                categoria.getId(),
                categoria.getNombreCategoria(),
                nombreCategoriaPadre,
                idCategoriaPadre,
                categoria.getSubcategorias().stream()
                        .map(Categoria::getNombreCategoria)
                        .toList(),
                categoria.getSubcategorias().stream()
                        .map(Categoria::getId)
                        .toList(),
                categoria.getListaProductos().stream().map(ProductoDto::of).toList());
    }
}
