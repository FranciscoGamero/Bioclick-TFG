package com.salesianostriana.bioclick.controller;


import com.salesianostriana.bioclick.dto.PaginacionDto;
import com.salesianostriana.bioclick.dto.favorito.FavoritoDto;
import com.salesianostriana.bioclick.dto.valoracion.ValoracionDto;
import com.salesianostriana.bioclick.model.User;
import com.salesianostriana.bioclick.service.FavoritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/favorite/")
@Tag(name = "Favorito", description = "El controlador de favoritos, para poder realizar todas las operaciones de gestión")

public class FavoritoController {

    private final FavoritoService favoritoService;

    @Operation(summary = "Agrega un producto a favorito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Producto agregado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FavoritoDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "nombreUsuario": "johndoe",
                                                    "nombreProducto": "Teclado Mecánico Eco",
                                                    "fechaFavorito": "2025-02-24T19:20:09.2607207"
                                                }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Solicitud incorrecta o datos inválidos",
                    content = @Content)
    })
    @PostMapping("/add/{productoId}")
    public ResponseEntity<FavoritoDto> addFavorito(@AuthenticationPrincipal User user, @PathVariable UUID productoId) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(FavoritoDto.of(favoritoService.createFavorito(user.getId(), productoId)));
    }

    @Operation(summary = "Obtiene todas los productos favoritos del usuario que realiza la peticion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado valoraciones",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FavoritoDto.class),
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
                                                                    "fechaFavorito": "2025-02-24T19:39:32.500506"
                                                                }
                                                            ]
                                                        }
                                """
                            )}
                    )}),
    })
    @GetMapping("/get/me")
    public PaginacionDto<FavoritoDto> listarFavoritos(@PageableDefault(page=0, size=4) Pageable pageable, @AuthenticationPrincipal User user) {

        return PaginacionDto.of(favoritoService.buscarFavoritos(pageable, user.getId())
                .map(FavoritoDto::of));
    }

    @Operation(summary = "Elimina una valoracion por su Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Producto eliminado correctamente",
                    content = @Content),

    })
    @DeleteMapping("/delete/{idProducto}")
    public ResponseEntity<?> eliminarFavorito(@PathVariable UUID idProducto, @AuthenticationPrincipal User user) {
        favoritoService.eliminarFavoritoPorId(user.getId(), idProducto);
        return ResponseEntity.noContent().build();
    }
}
