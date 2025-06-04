package com.salesianostriana.bioclick.service;

import com.salesianostriana.bioclick.dto.Categoria.CreateCategoriaDto;
import com.salesianostriana.bioclick.model.Categoria;
import com.salesianostriana.bioclick.repository.CategoriaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final EntityManager entityManager;



    public Categoria crearCategoria(CreateCategoriaDto createCategoriaDto) {
        Categoria padre = categoriaRepository.findById(UUID.fromString
                (createCategoriaDto.categoriaPadreId())).orElseThrow(() -> new EntityNotFoundException("Categoria padre no encontrada"));

        Set<Categoria> listaSubcategorias = new HashSet<>(categoriaRepository.findAllById(createCategoriaDto.subcategoriaIds()
                .stream()
                .map(UUID::fromString)
                .collect(Collectors.toSet())));


        return categoriaRepository.save(Categoria.builder()
                .nombreCategoria(createCategoriaDto.nombreCategoria())
                .categoriaPadre(padre)
                .subcategorias(listaSubcategorias)
                .build());

    }

    public Page<Categoria> buscarCategorias(Pageable pageable, boolean borrado) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("categoriaBorradoFiltro");
        filter.setParameter("isBorrado", borrado);
        Page<Categoria> categorias = categoriaRepository.buscarCategorias(pageable);
        session.disableFilter("categoriaBorradoFiltro");
        return categorias;
    }
    @Transactional
    public Categoria obtenerCategoria(UUID id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"));

        Hibernate.initialize(categoria.getSubcategorias());

        return categoria;
    }

    public Categoria editarCategoria(CreateCategoriaDto createCategoriaDto, UUID id) {

        Categoria padre = Optional.ofNullable(createCategoriaDto.categoriaPadreId())
                .filter(idStr -> !idStr.isEmpty())
                .map(UUID::fromString)
                .flatMap(categoriaRepository::findById)
                .orElse(null);

        Set<Categoria> listaSubcategorias = Optional.ofNullable(createCategoriaDto.subcategoriaIds())
                .map(subIds -> subIds.stream()
                        .map(UUID::fromString)
                        .collect(Collectors.toSet()))
                .map(categoriaRepository::findAllById)
                .map(HashSet::new)
                .orElseGet(HashSet::new);

        return categoriaRepository.findById(id)
                .map(old -> {
                    old.setNombreCategoria(createCategoriaDto.nombreCategoria());
                    old.setCategoriaPadre(padre);
                    old.setSubcategorias(listaSubcategorias);
                    return categoriaRepository.save(old);
                })
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"));
    }


    @Transactional
    public void eliminarCategoriaPorId(UUID id) {
        categoriaRepository.eliminarProductosPorCategoria(id);
        categoriaRepository.deleteById(id);
    }
}
