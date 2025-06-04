package com.salesianostriana.bioclick.controller;

import com.salesianostriana.bioclick.dto.PaginacionDto;
import com.salesianostriana.bioclick.dto.producto.CreateProductoDto;
import com.salesianostriana.bioclick.dto.producto.ProductoDto;
import com.salesianostriana.bioclick.dto.valoracion.CreateValoracionDto;
import com.salesianostriana.bioclick.dto.valoracion.EditValoracionDto;
import com.salesianostriana.bioclick.dto.valoracion.ValoracionDto;
import com.salesianostriana.bioclick.model.User;
import com.salesianostriana.bioclick.model.Valoracion;
import com.salesianostriana.bioclick.service.ValoracionService;
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
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/valoration/")
@Tag(name = "Valoracion", description = "El controlador de valoracion, para poder realizar todas las operaciones de gestión")

public class ValoracionController {

    private final ValoracionService valoracionService;

    @Operation(summary = "Crea una nueva valoracion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Valoracion registrada correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValoracionDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "nombreUsuario": "johndoe",
                                                    "nombreProducto": "Teclado Mecánico Eco",
                                                    "comentario": "El producto es excelente, de buena calidad y ecológico.",
                                                    "puntuacion": 4.5,
                                                    "fechaValorado": "2025-02-24T17:22:50.0803424"
                                                }
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Solicitud incorrecta o datos inválidos",
                    content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<ValoracionDto> createValoracion(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Producto a crear", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateValoracionDto.class),
                    examples = @ExampleObject(value = """
                                                {
                                                  "puntuacion": 4.5,
                                                  "comentario": "El producto es excelente, de buena calidad y ecológico.",
                                                  "user_id": "d290f1ee-6c54-4b01-90e6-d701748f0851",
                                                  "producto_id": "4a5b6c7d-8e9f-1234-abcd-567890123456"
                                                }
                                      
                            """))) @RequestBody @Valid CreateValoracionDto createValoracionDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ValoracionDto.of(valoracionService.createValoracion(createValoracionDto)));
    }

    @Operation(summary = "Obtiene todas las valoraciones de un producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado valoraciones",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValoracionDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                    {
                                                        "numPagina": 0,
                                                        "tamanioPagina": 4,
                                                        "elementosEncontrados": 3,
                                                        "paginasTotales": 1,
                                                        "contenido": [
                                                            {
                                                                "nombreUsuario": "patriciajones",
                                                                "nombreProducto": "Cámara de Seguridad Reciclada",
                                                                "comentario": "Muy buen rendimiento y diseño, volveré a comprar.",
                                                                "puntuacion": 4.8,
                                                                "fechaValorado": "2025-02-24T00:00:00"
                                                            },
                                                            {
                                                                "nombreUsuario": "lisawilliams",
                                                                "nombreProducto": "Cámara de Seguridad Reciclada",
                                                                "comentario": "Buen producto, pero los materiales podrían ser mejores.",
                                                                "puntuacion": 3.5,
                                                                "fechaValorado": "2025-02-24T00:00:00"
                                                            },
                                                            {
                                                                "nombreUsuario": "jamesmoore",
                                                                "nombreProducto": "Cámara de Seguridad Reciclada",
                                                                "comentario": "El mejor producto que he comprado hasta ahora, excelente.",
                                                                "puntuacion": 5.0,
                                                                "fechaValorado": "2025-02-24T00:00:00"
                                                            }
                                                        ]
                                                    }
                                """
                            )}
                    )}),
    })
    @GetMapping("/get/{idProducto}")
    public PaginacionDto<ValoracionDto> listarValoracionPorProducto(@PageableDefault(page=0, size=4) Pageable pageable, @PathVariable UUID idProducto) {

        return PaginacionDto.of(valoracionService.buscarValoracionesProducto(pageable, idProducto)
                .map(ValoracionDto::of));
    }

    @Operation(summary = "Obtiene todas las valoraciones del usuario que realiza la peticion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado valoraciones",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValoracionDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "numPagina": 0,
                                                    "tamanioPagina": 4,
                                                    "elementosEncontrados": 1,
                                                    "paginasTotales": 1,
                                                    "contenido": [
                                                        {
                                                            "nombreUsuario": "johndoe",
                                                            "nombreProducto": "Teclado Mecánico Eco",
                                                            "comentario": "Producto de excelente calidad, muy satisfecho con mi compra.",
                                                            "puntuacion": 4.5,
                                                            "fechaValorado": "2025-02-24T00:00:00"
                                                        }
                                                    ]
                                                }
                                """
                            )}
                    )}),
    })
    @GetMapping("/get/me")
    public PaginacionDto<ValoracionDto> listarValoracionesPropias(@PageableDefault(page=0, size=4) Pageable pageable, @AuthenticationPrincipal User user) {

        return PaginacionDto.of(valoracionService.buscarValoracionesUsuario(pageable, user.getId())
                .map(ValoracionDto::of));
    }
    @Operation(summary = "Obtiene la valoracion de un producto por su id autogenerado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado valoraciones",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValoracionDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "nombreUsuario": "patriciajones",
                                                    "nombreProducto": "Cámara de Seguridad Reciclada",
                                                    "comentario": "Muy buen rendimiento y diseño, volveré a comprar.",
                                                    "puntuacion": 4.8,
                                                    "fechaValorado": "2025-02-24T00:00:00"
                                                }
                                """
                            )}
                    )})
    })
    @GetMapping("/get-one/{valoracionId}")
    public ValoracionDto verUnaValoracion(@PathVariable UUID valoracionId) {

        return ValoracionDto.of(valoracionService.buscarValoracionPorId(valoracionId));
    }

    @Operation(summary = "Actualiza un producto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Producto actualizado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "nombreUsuario": "lisawilliams",
                                                    "nombreProducto": "Cámara de Seguridad Reciclada",
                                                    "comentario": "El producto es excelente, de buena calidad y ecológico.",
                                                    "puntuacion": 4.5,
                                                    "fechaValorado": "2025-02-24T00:00:00"
                                                }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ningún producto con ese ID",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para la actualización",
                    content = @Content)
    })
    @PutMapping("/edit/{valoracionId}")
    public ValoracionDto editarProducto(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Producto a editar", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductoDto.class),
                    examples = @ExampleObject(value = """
                                                        {
                                                            "puntuacion": 4.5,
                                                            "comentario": "El producto es excelente, de buena calidad y ecológico."
                                                        }
                            """))) @PathVariable UUID valoracionId, @RequestBody @Valid EditValoracionDto editValoracionDto){

        return ValoracionDto.of(valoracionService.editarValoracion(editValoracionDto, valoracionId));
    }

    @Operation(summary = "Elimina una valoracion por su Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Producto eliminado correctamente",
                    content = @Content),

    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        valoracionService.eliminarAsociacionPorId(id, user.getId());
        return ResponseEntity.noContent().build();
    }

}
