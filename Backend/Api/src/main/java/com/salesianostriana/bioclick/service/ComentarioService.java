package com.salesianostriana.bioclick.service;

import com.salesianostriana.bioclick.dto.Comentario.CreateComentarioDto;
import com.salesianostriana.bioclick.error.CommentException;
import com.salesianostriana.bioclick.model.Comentario;
import com.salesianostriana.bioclick.model.Producto;
import com.salesianostriana.bioclick.model.User;
import com.salesianostriana.bioclick.repository.ComentarioRepository;
import com.salesianostriana.bioclick.repository.ProductoRepository;
import com.salesianostriana.bioclick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final UserRepository userRepository;
    private final ProductoRepository productoRepository;

    public Comentario crearComentario(CreateComentarioDto createComentarioDto) {

        Optional<User> usuario = userRepository.findById(createComentarioDto.userId());
        Optional<Producto> producto = productoRepository.findById(createComentarioDto.productoId());

        if (usuario.isPresent() && producto.isPresent()) {
            return comentarioRepository.save(Comentario.builder()
                    .comentario(createComentarioDto.comentario())
                    .fechaComentario(LocalDateTime.now())
                    .usuario(usuario.get())
                    .producto(producto.get())
                    .build());
        } else {
            throw new CommentException();
        }
    }
}
