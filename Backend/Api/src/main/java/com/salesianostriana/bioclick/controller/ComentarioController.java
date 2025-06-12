package com.salesianostriana.bioclick.controller;


import com.salesianostriana.bioclick.dto.Categoria.CategoriaDto;
import com.salesianostriana.bioclick.dto.Categoria.CreateCategoriaDto;
import com.salesianostriana.bioclick.dto.Comentario.ComentarioDto;
import com.salesianostriana.bioclick.dto.Comentario.CreateComentarioDto;
import com.salesianostriana.bioclick.dto.Comentario.EditComentarioDto;
import com.salesianostriana.bioclick.dto.ImpactoAmbiental.ImpactoDto;


import com.salesianostriana.bioclick.dto.PaginacionDto;
import com.salesianostriana.bioclick.model.User;
import com.salesianostriana.bioclick.service.ComentarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment/")
@Tag(name = "Categoria", description = "El controlador de los comentarios, para poder realizar todas las operaciones de gestión")
public class ComentarioController {

    private final ComentarioService comentarioService;

    @Operation(summary = "Crea un nuevo comentario para un producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Comentario registrado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ComentarioDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "id": "64715ddd-b22f-40db-9c46-65b3157efe50",
                                                    "comentario": "comentario prueba",
                                                    "usuarioId": "16f857b1-bf58-442f-ae41-9cd96f786345",
                                                    "productoId": "def45678-9012-3456-ab78-901234567890"
                                                }
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Solicitud incorrecta o datos inválidos",
                    content = @Content)
    })
    @PostMapping("/add")
    public ResponseEntity<ComentarioDto> crearComentario(@RequestBody CreateComentarioDto createComentarioDto){
        System.out.println(createComentarioDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ComentarioDto.of(comentarioService.crearComentario(createComentarioDto)));
    }

    @Operation(summary = "Actualiza un comentario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Comentario actualizado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ComentarioDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                        {
                                            "comentarioCambiado": "Comentario cambiado"
                                        }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ningun comentario con ese ID",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para la actualización",
                    content = @Content)
    })
    @PutMapping("/edit/{comentarioId}")
    public ResponseEntity<ComentarioDto> editarComentario(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Comentario a editar", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateCategoriaDto.class),
                    examples = @ExampleObject(value = """
                                                    {
                                                        "id": "81e2dcef-fda6-4f24-bb78-7e62c0b5e1c1",
                                                        "comentario": "Comentario cambiado",
                                                        "usuarioId": "d290f1ee-6c54-4b01-90e6-d701748f0851",
                                                        "productoId": "4a5b6c7d-8e9f-1234-abcd-567890123456"
                                                    }
                            """))) @PathVariable UUID comentarioId, @RequestBody EditComentarioDto comentarioDto, @AuthenticationPrincipal User user){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ComentarioDto.of(comentarioService.editarComentario(comentarioDto.comentarioCambiado(), comentarioId, user)));
    }
    @Operation(summary = "Elimina un comentario por su Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Comentario eliminado correctamente",
                    content = @Content),

    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> eliminarComentario(@PathVariable UUID id, @AuthenticationPrincipal User user){
        comentarioService.eliminarComentarioPorId(id, user);
        return ResponseEntity.noContent().build();
    }
                                              @Operation(summary = "Obtiene todos los comentarios de un producto dado su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Comentarios recuperados correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comentario.class),
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "idComentario": "5a5d9e7e-d5ec-41e2-ae3c-9f8e7a9373b9",
                                            "fechaComentario": "2025-06-12T15:32:10",
                                            "comentario": "Muy buen producto, llegó rápido y en buen estado",
                                            "usuario": {
                                                "id": "f8c3a2b2-e1e4-4b8d-a7bb-c2d26d15a5a9",
                                                "nombre": "Juan Pérez"
                                            },
                                            "producto": {
                                                "id": "df1c21f0-b519-42d4-a68c-e5c26e18a7ab",
                                                "nombreProducto": "Power Bank Solar"
                                            }
                                        }
                                    ]
                            """))
            ),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ningún producto con ese ID o no hay comentarios",
                    content = @Content)
    })
    @GetMapping("/product/{productoId}")
    public PaginacionDto<ComentarioDto> listarComentariosPorProducto(
            @PathVariable UUID productoId, Pageable pageable) {
        return PaginacionDto.of(comentarioService.listarComentariosPorProductoId(productoId, pageable).map(ComentarioDto::of));
}
