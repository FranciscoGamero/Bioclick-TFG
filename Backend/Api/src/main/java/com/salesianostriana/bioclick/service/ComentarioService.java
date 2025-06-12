package com.salesianostriana.bioclick.service;

import com.salesianostriana.bioclick.dto.Comentario.CreateComentarioDto;
import com.salesianostriana.bioclick.dto.ImpactoAmbiental.CreateImpactoDto;
import com.salesianostriana.bioclick.error.CommentException;
import com.salesianostriana.bioclick.model.Comentario;
import com.salesianostriana.bioclick.model.ImpactoAmbiental;
import com.salesianostriana.bioclick.model.Producto;
import com.salesianostriana.bioclick.model.User;
import com.salesianostriana.bioclick.repository.ComentarioRepository;
import com.salesianostriana.bioclick.repository.ProductoRepository;
import com.salesianostriana.bioclick.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


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


    public Comentario editarComentario(String comentarioCambiado, UUID comentarioId, User usuario) {

        Optional<Comentario> comentarioAEditar = comentarioRepository.findById(comentarioId);

        if (usuario.getId() == comentarioAEditar.get().getIdComentario() || Objects.equals(usuario.getRole(), "ROLE_ADMIN")
                || Objects.equals(usuario.getRole(), "ROLE_MANAGER")) {
            return comentarioRepository.findById(comentarioId).map(old -> {
                old.setComentario(comentarioCambiado);
                return comentarioRepository.save(old);
            }).orElseThrow(() -> new EntityNotFoundException("No se pudo editar dicho comentario" + comentarioId));

        }
        else{
            throw new CommentException("No puedes permisos para editar este comentario");
        }
    }

    public void eliminarComentarioPorId(UUID id, User usuario) {

        Optional<Comentario> comentarioABorrar = comentarioRepository.findById(id);

        if (usuario.getId() == comentarioABorrar.get().getUsuario().getId() || Objects.equals(usuario.getRole(), "ROLE_ADMIN")
                || Objects.equals(usuario.getRole(), "ROLE_MANAGER")) {
            comentarioRepository.deleteById(id);
        }
        else{
            throw new CommentException("No puedes permisos para eliminar este comentario");
        }
    }
      public Page<Comentario> listarComentariosPorProductoId(UUID productoId, Pageable pageable) {
        return comentarioRepository.findByProductoId(productoId, pageable);
}
