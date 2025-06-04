package com.salesianostriana.bioclick.service;

import com.salesianostriana.bioclick.dto.valoracion.CreateValoracionDto;
import com.salesianostriana.bioclick.dto.valoracion.EditValoracionDto;
import com.salesianostriana.bioclick.model.Producto;
import com.salesianostriana.bioclick.model.User;
import com.salesianostriana.bioclick.model.Valoracion;
import com.salesianostriana.bioclick.model.ValoracionPK;
import com.salesianostriana.bioclick.repository.ProductoRepository;
import com.salesianostriana.bioclick.repository.UserRepository;
import com.salesianostriana.bioclick.repository.ValoracionRepository;
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
public class ValoracionService {

    private final ValoracionRepository valoracionRepository;
    private final UserRepository userRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public Valoracion createValoracion(CreateValoracionDto createValoracionDto){

        ValoracionPK valoracionPK = ValoracionPK.builder()
                .producto_id(UUID.fromString(createValoracionDto.producto_id()))
                .usuario_id(UUID.fromString(createValoracionDto.user_id()))
                .build();

        User user = userRepository.findById(valoracionPK.getUsuario_id())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el usuario"));

        Producto producto = productoRepository.findById(valoracionPK.getProducto_id())
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el producto"));


        Valoracion valoracion =  valoracionRepository.save(Valoracion.builder()
                        .valoracionPK(valoracionPK)
                        .puntuacion(createValoracionDto.puntuacion())
                        .comentario(createValoracionDto.comentario())
                        .fechaValorado(LocalDateTime.now())
                        .usuario(user)
                        .producto(producto)
                .build());

        user.addValoracion(valoracion);
        return valoracionRepository.save(valoracion);
    }

    public Page<Valoracion> buscarValoracionesProducto(Pageable pageable, UUID idProducto) {
        return valoracionRepository.buscarValoracionesDeProducto(idProducto, pageable);
    }

    public Page<Valoracion> buscarValoracionesUsuario(Pageable pageable, UUID userid) {
        return valoracionRepository.buscarValoracionesUsuario(pageable, userid);
    }

    public Valoracion buscarValoracionPorId(UUID valoracionId) {
        return valoracionRepository.buscarPorIdAutogenerado(valoracionId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la valoracion"));
    }

    public Valoracion editarValoracion(EditValoracionDto editValoracionDto, UUID valoracionId) {
        return valoracionRepository.buscarPorIdAutogenerado(valoracionId).map(old -> {

            old.setComentario(editValoracionDto.comentario());
            old.setPuntuacion(editValoracionDto.puntuacion());
            return valoracionRepository.save(old);
        }).orElseThrow(() -> new EntityNotFoundException("No se pudo editar dicha valoracion" + valoracionId));
    }

    @Transactional
    public void eliminarAsociacionPorId(UUID id, UUID user) {

        User userEncontrado = userRepository.findById(user).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));


        Hibernate.initialize(userEncontrado.getListaValoraciones());

        Optional<Valoracion> valoracionAEliminar = userEncontrado.getListaValoraciones()
                .stream()
                .filter(valoracion -> valoracion.getValoracionPK().getId_generado().equals(id))
                .findFirst();


        if (valoracionAEliminar.isPresent()) {
            userEncontrado.removeValoracion(valoracionAEliminar.get());
            userRepository.save(userEncontrado);
            valoracionRepository.delete(valoracionAEliminar.get());
        } else {
            throw new EntityNotFoundException("La valoración con ID " + id + " no fue encontrada en las valoraciones del usuario.");
        }
    }
}
