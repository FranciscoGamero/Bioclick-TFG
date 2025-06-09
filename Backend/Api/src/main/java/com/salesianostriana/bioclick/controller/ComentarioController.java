package com.salesianostriana.bioclick.controller;


import com.salesianostriana.bioclick.dto.Comentario.ComentarioDto;
import com.salesianostriana.bioclick.dto.Comentario.CreateComentarioDto;
import com.salesianostriana.bioclick.dto.ImpactoAmbiental.ImpactoDto;

import com.salesianostriana.bioclick.service.ComentarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                            schema = @Schema(implementation = ImpactoDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                    {
                                                        "nombreProducto": "Cámara de Seguridad Reciclada",
                                                        "reduccionCo2": 19.7,
                                                        "ahorroMateriales": [
                                                            "Plástico reciclado",
                                                            "Aluminio reciclado",
                                                            "Fibra de bambú"
                                                        ]
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
}
