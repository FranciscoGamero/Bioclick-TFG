package com.salesianostriana.bioclick.controller;

import com.salesianostriana.bioclick.dto.ImpactoAmbiental.CreateImpactoDto;
import com.salesianostriana.bioclick.dto.ImpactoAmbiental.ImpactoDto;
import com.salesianostriana.bioclick.dto.PaginacionDto;
import com.salesianostriana.bioclick.dto.admin.AdminDto;
import com.salesianostriana.bioclick.dto.producto.CreateProductoDto;
import com.salesianostriana.bioclick.dto.producto.ProductoDto;
import com.salesianostriana.bioclick.model.User;
import com.salesianostriana.bioclick.service.ImpactoAmbientalService;
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
@RequestMapping("/impact/")
@Tag(name = "Impacto Ambiental", description = "El controlador de impacto ambiental, para poder realizar todas las operaciones de gestión")

public class ImpactoAmbientalController {

    private final ImpactoAmbientalService impactoAmbientalService;

    @Operation(summary = "Crea un nuevo impacto para un producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Impacto registrado correctamente",
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
    @PostMapping("/create")
    public ResponseEntity<ImpactoDto> createImpacto(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Impacto a crear", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateImpactoDto.class),
                    examples = @ExampleObject(value = """
                                                    {
                                                        "productoId": "def45678-9012-3456-ab78-901234567890",
                                                        "reduccionCo2": 19.7,
                                                        "ahorroMateriales": [
                                                            "Plástico reciclado",
                                                            "Aluminio reciclado",
                                                            "Fibra de bambú"
                                                            ]
                                                    }                                   
                            """))) @RequestBody @Valid CreateImpactoDto createImpactoDto, @AuthenticationPrincipal User user) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ImpactoDto.of(impactoAmbientalService.createImpactoAmbiental(createImpactoDto)));
    }

    @Operation(summary = "Obtiene todos los impactos creados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado impactos",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ImpactoDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "numPagina": 0,
                                                    "tamanioPagina": 4,
                                                    "elementosEncontrados": 17,
                                                    "paginasTotales": 5,
                                                    "contenido": [
                                                        {
                                                            "nombreProducto": "Teclado Mecánico Eco",
                                                            "reduccionCo2": 12.5,
                                                            "ahorroMateriales": [
                                                                "Plástico reciclado",
                                                                "Metal reutilizado"
                                                            ]
                                                        },
                                                        {
                                                            "nombreProducto": "Ratón Inalámbrico Sostenible",
                                                            "reduccionCo2": 8.0,
                                                            "ahorroMateriales": [
                                                                "Baterías recicladas",
                                                                "Plásticos reutilizados"
                                                            ]
                                                        },
                                                        {
                                                            "nombreProducto": "Altavoz Bluetooth Eco",
                                                            "reduccionCo2": 10.2,
                                                            "ahorroMateriales": [
                                                                "Componentes electrónicos reutilizados",
                                                                "Madera reciclada"
                                                            ]
                                                        },
                                                        {
                                                            "nombreProducto": "Smartphone Eco-Friendly",
                                                            "reduccionCo2": 18.7,
                                                            "ahorroMateriales": [
                                                                "Plástico reciclado",
                                                                "Vidrio reciclado"
                                                            ]
                                                        }
                                                    ]
                                                }
                                """
                            )}
                    )}),
    })
    @GetMapping("/get/all")
    public PaginacionDto<ImpactoDto> listarImpactos(@PageableDefault(page=0, size=4) Pageable pageable) {

        return PaginacionDto.of(impactoAmbientalService.buscarImpactos(pageable)
                .map(ImpactoDto::of));
    }
    @Operation(summary = "Obtiene un impacto creado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado el impacto",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ImpactoDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "nombreProducto": "Teclado Mecánico Eco",
                                                    "reduccionCo2": 12.5,
                                                    "ahorroMateriales": [
                                                        "Plástico reciclado",
                                                        "Metal reutilizado"
                                                    ]
                                                }
                                """
                            )}
                    )}),
    })
    @GetMapping("/get/{impactoID}")
    public ImpactoDto mostrarImpacto(@PathVariable UUID impactoID) {

        return ImpactoDto.of(impactoAmbientalService.buscarUnImpacto(impactoID));
    }

    @Operation(summary = "Actualiza un impacto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Impacto actualizado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ImpactoDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                    {
                                                        "nombreProducto": "Teclado Mecánico Eco",
                                                        "reduccionCo2": 19.7,
                                                        "ahorroMateriales": [
                                                            "Plástico reciclado",
                                                            "Aluminio reciclado",
                                                            "Acero reforzado reciclado",
                                                            "Fibra de bambú"
                                                        ]
                                                    }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ningún impacto con ese ID",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para la actualización",
                    content = @Content)
    })
    @PutMapping("/edit/{impactId}")
    public ImpactoDto editarImapcto(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Impacto a editar", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ImpactoDto.class),
                    examples = @ExampleObject(value = """
                                                        {
                                                            "productoId": "4a5b6c7d-8e9f-1234-abcd-567890123456",
                                                            "reduccionCo2": 19.7,
                                                            "ahorroMateriales": [
                                                                "Plástico reciclado",
                                                                "Aluminio reciclado",
                                                                "Acero reforzado reciclado",
                                                                "Fibra de bambú"
                                                                ]
                                                        }
                            """))) @PathVariable UUID impactId, @RequestBody @Valid CreateImpactoDto editImpactDto){

        return ImpactoDto.of(impactoAmbientalService.editarImpacto(editImpactDto, impactId));
    }
}
