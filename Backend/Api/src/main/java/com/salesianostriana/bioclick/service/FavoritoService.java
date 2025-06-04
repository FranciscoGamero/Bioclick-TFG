package com.salesianostriana.bioclick.service;

import com.salesianostriana.bioclick.model.*;
import com.salesianostriana.bioclick.repository.FavoritoRepository;
import com.salesianostriana.bioclick.repository.ProductoRepository;
import com.salesianostriana.bioclick.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final UserRepository userRepository;
    private final ProductoRepository productoRepository;


    @Transactional
    public Favorito createFavorito(UUID userId, UUID productoId){

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("No se ha encontrado un usuario con es id"));
        Producto producto = productoRepository.findById(productoId).orElseThrow(() -> new EntityNotFoundException("No se ha encontrado un producto"));

        FavoritoPK favoritoPK = new FavoritoPK(userId, productoId);

        Favorito favorito = Favorito.builder()
                .favoritoPK(favoritoPK)
                .usuario(user)
                .producto(producto)
                .fechaFavorito(LocalDateTime.now())
                .build();

        user.addFavorito(favorito);
        return favoritoRepository.save(favorito);
    }

    public Page<Favorito> buscarFavoritos(Pageable pageable, UUID id) {
        return favoritoRepository.buscarPorId(pageable, id);
    }
    @Transactional
    public void eliminarFavoritoPorId(UUID userid, UUID productoId) {

        User userEncontrado = userRepository.findById(userid).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));


        Hibernate.initialize(userEncontrado.getListaFavoritos());

        Optional<Favorito> favoritoAEliminar = userEncontrado.getListaFavoritos()
                .stream()
                .filter(favorito -> favorito.getFavoritoPK().getProducto_id().equals(productoId))
                .findFirst();

        if (favoritoAEliminar.isPresent()) {
            userEncontrado.removeFavorito(favoritoAEliminar.get());
            userRepository.save(userEncontrado);
            favoritoRepository.delete(favoritoAEliminar.get());
        } else {
            throw new EntityNotFoundException("La valoración con no fue encontrada en las valoraciones del usuario.");
        }
    }
}
