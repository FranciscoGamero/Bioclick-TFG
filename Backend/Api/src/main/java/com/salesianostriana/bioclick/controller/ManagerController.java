package com.salesianostriana.bioclick.controller;

import com.salesianostriana.bioclick.dto.PaginacionDto;
import com.salesianostriana.bioclick.dto.manager.CreateManagerDto;
import com.salesianostriana.bioclick.dto.manager.EditManagerDto;
import com.salesianostriana.bioclick.dto.manager.ManagerDto;
import com.salesianostriana.bioclick.dto.producto.CreateProductoDto;
import com.salesianostriana.bioclick.dto.producto.ProductoDto;
import com.salesianostriana.bioclick.dto.user.UserDto;
import com.salesianostriana.bioclick.model.Admin;
import com.salesianostriana.bioclick.model.Manager;
import com.salesianostriana.bioclick.model.Producto;
import com.salesianostriana.bioclick.model.User;
import com.salesianostriana.bioclick.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequiredArgsConstructor
@RequestMapping("/manager/")
@Tag(name = "Manager", description = "El controlador de manager, para poder realizar todas las operaciones de gestión")

public class ManagerController {

    private final ManagerService managerService;

    @Operation(summary = "Crea un nuevo manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Manager registrado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ManagerDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                    {
                                                        "username": "nuevoManager",
                                                        "correo": "nuevoManager@example.com",
                                                        "fotoPerfilUrl": "http://localhost:8080/download/01%20ACT%201%20WORK%20VOCABULARY.png",
                                                        "ultimaAccion": "None",
                                                        "fechaUltimaAccion": "2025-02-25T13:19:40.3658129"
                                                    }
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Solicitud incorrecta o datos inválidos",
                    content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<ManagerDto> createManager(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Producto a crear", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ManagerDto.class),
                    examples = @ExampleObject(value = """
                                                                                    {
                                                                                      "username": "nuevoManager",
                                                                                      "password": "Password123",
                                                                                      "verifyPassword": "Password123",
                                                                                      "correo": "nuevoManager@example.com"
                                                                                    }
                            """))) @RequestPart("crear") @Valid CreateManagerDto createManagerDto,
                                                    @RequestPart("file") MultipartFile file,
                                                    @AuthenticationPrincipal User user) {

        Manager manager = managerService.createManager(createManagerDto, user.getId(), file);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ManagerDto.of(manager, getImageUrl(manager.getFotoPerfil())));
    }


    @Operation(summary = "Edita al manager que realiza la peticion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Manager editado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ManagerDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "username": "lucasEdit",
                                                    "correo": "correoCambiado@example.com",
                                                    "fotoPerfilUrl": "http://localhost:8080/download/image-removebg-preview%20(3)_136312.png",
                                                    "ultimaAccion": "Creación de producto",
                                                    "fechaUltimaAccion": "2024-02-10T14:45:00"
                                                }
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Solicitud incorrecta o datos inválidos",
                    content = @Content)
    })
    @PutMapping("/edit/me")
    public ResponseEntity<ManagerDto> editManager(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Manager a editar", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ManagerDto.class),
                    examples = @ExampleObject(value = """
                                                                        {
                                                                          "username": "lucasEdit",
                                                                          "correo": "correoCambiado@example.com",
                                                                          "password": "passwordCambiada"
                                                                        }
                            """))) @RequestPart("editar") @Valid EditManagerDto editManagerDto,
                                                  @RequestPart("file") MultipartFile file,
                                                  @AuthenticationPrincipal Manager manager) {

        Manager managerEditado = managerService.managerEditarse(editManagerDto, manager, file);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ManagerDto.of(managerEditado, getImageUrl(managerEditado.getFotoPerfil())));
    }

    @Operation(summary = "Lista todos los managers registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Manager encontrados correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ManagerDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "numPagina": 0,
                                                "tamanioPagina": 10,
                                                "elementosEncontrados": 2,
                                                "paginasTotales": 1,
                                                "contenido": [
                                                    {
                                                        "username": "emilywatson",
                                                        "correo": "emily.watson@example.com",
                                                        "fotoPerfilUrl": "http://localhost:8080/download/emilywatson.jpg",
                                                        "ultimaAccion": "Actualización de producto",
                                                        "fechaUltimaAccion": "2024-02-18T16:30:00"
                                                    },
                                                    {
                                                        "username": "lucasgreen",
                                                        "correo": "lucas.green@example.com",
                                                        "fotoPerfilUrl": "http://localhost:8080/download/lucasgreen.png",
                                                        "ultimaAccion": "Creación de producto",
                                                        "fechaUltimaAccion": "2024-02-10T14:45:00"
                                                    }
                                                ]
                                            }
                                            """
                            )})
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Solicitud incorrecta o datos inválidos",
                    content = @Content)
    })
    @GetMapping("/get/all")
    public ResponseEntity<PaginacionDto<ManagerDto>> buscarManagers(
            @RequestParam(value = "nombre", required = false) String nombreUsuario,
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal User user) {

        Page<ManagerDto> managers = managerService.buscarTodos(user.getId(), pageable, false)
                .map(manager -> ManagerDto.of(manager, getImageUrl(manager.getFotoPerfil())));

        return ResponseEntity.ok(PaginacionDto.of(managers));
    }


    public String getImageUrl(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();
    }


}
