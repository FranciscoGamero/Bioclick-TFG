package com.salesianostriana.bioclick.controller;

import com.salesianostriana.bioclick.dto.Categoria.CategoriaDto;
import com.salesianostriana.bioclick.dto.Categoria.CreateCategoriaDto;
import com.salesianostriana.bioclick.dto.PaginacionDto;
import com.salesianostriana.bioclick.dto.admin.AdminDto;
import com.salesianostriana.bioclick.dto.admin.CreateAdminRequest;
import com.salesianostriana.bioclick.dto.user.EditUserDto;
import com.salesianostriana.bioclick.dto.user.UserDto;
import com.salesianostriana.bioclick.model.Categoria;
import com.salesianostriana.bioclick.model.User;
import com.salesianostriana.bioclick.service.CategoriaService;
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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category/")
@Tag(name = "Categoria", description = "El controlador de categoria, para poder realizar todas las operaciones de gestión")

public class CategoriaController {

    private final CategoriaService categoriaService;

    @Operation(summary = "Crea una nueva categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Administrador registrado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "nombreCategoria": "Portátiles Gaming",
                                                    "nombreCategoriaPadre": "Portátiles",
                                                    "nombresSubcategorias": []
                                                }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Solicitud incorrecta o datos inválidos",
                    content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<CategoriaDto> createCategoria(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Categoria a crear", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateCategoriaDto.class),
                    examples = @ExampleObject(value = """
                                                    {
                                                      "nombreCategoria": "Portátiles Gaming",
                                                      "categoriaPadreId": "6a7b8c9d-e89b-12d3-a456-426614174005",
                                                      "subcategoriaIds": []
                                                    }                                            
                            """))) @RequestBody @Valid CreateCategoriaDto createCategoriaDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CategoriaDto.of(categoriaService.crearCategoria(createCategoriaDto)));
    }
    @Operation(summary = "Obtiene todas las categorias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado categorias",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "numPagina": 0,
                                                    "tamanioPagina": 4,
                                                    "elementosEncontrados": 10,
                                                    "paginasTotales": 3,
                                                    "contenido": [
                                                        {
                                                            "nombreCategoria": "Dispositivos Electrónicos",
                                                            "nombreCategoriaPadre": "None",
                                                            "nombresSubcategorias": [
                                                                "Teléfonos Inteligentes",
                                                                "Portátiles",
                                                                "Tablets",
                                                                "Smartwatches"
                                                            ]
                                                        },
                                                        {
                                                            "nombreCategoria": "Accesorios Tecnológicos",
                                                            "nombreCategoriaPadre": "None",
                                                            "nombresSubcategorias": []
                                                        },
                                                        {
                                                            "nombreCategoria": "Equipos de Oficina",
                                                            "nombreCategoriaPadre": "None",
                                                            "nombresSubcategorias": []
                                                        },
                                                        {
                                                            "nombreCategoria": "Teléfonos Inteligentes",
                                                            "nombreCategoriaPadre": "Dispositivos Electrónicos",
                                                            "nombresSubcategorias": []
                                                        }
                                                    ]
                                                }
                                """
                            )}
                    )}),
    })
    @GetMapping("/get/all")
    public PaginacionDto<CategoriaDto> listarCategorias(@PageableDefault(page=0, size=4) Pageable pageable) {

        return PaginacionDto.of(categoriaService.buscarCategorias(pageable, false)
                .map(CategoriaDto::of));
    }

    @Operation(summary = "Obtiene una categoria por su Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Categoria encontrada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "nombreCategoria": "Dispositivos Electrónicos",
                                                    "nombreCategoriaPadre": "None",
                                                    "nombresSubcategorias": [
                                                        "Smartwatches",
                                                        "Teléfonos Inteligentes",
                                                        "Tablets",
                                                        "Portátiles"
                                                    ]
                                                }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna categoria con ese ID",
                    content = @Content)
    })
    @GetMapping("/get/{categoryId}")
    public CategoriaDto obtenerUsuario(@PathVariable UUID categoryId) {
        return CategoriaDto.of(categoriaService.obtenerCategoria(categoryId));
    }

    @Operation(summary = "Actualiza una categoria existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Categoria actualizada correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "nombreCategoria": "Dispositivos Electrónicos",
                                                    "nombreCategoriaPadre": "None",
                                                    "nombresSubcategorias": [
                                                        "Teléfonos Inteligentes",
                                                        "Tablets"
                                                    ]
                                                }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna categoria con ese ID",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para la actualización",
                    content = @Content)
    })
    @PutMapping("/edit/{categoriaId}")
    public CategoriaDto editarUsuario(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Categoria a editar", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateCategoriaDto.class),
                    examples = @ExampleObject(value = """
                                                        {
                                                            "nombreCategoria": "Dispositivos Electrónicos",
                                                            "subcategoriaIds": [
                                                                "4a5b6c7d-e89b-12d3-a456-426614174003",
                                                                "5a6b7c8d-e89b-12d3-a456-426614174004"
                                                            ]
                                                        }
                            """))) @PathVariable UUID categoriaId, @RequestBody @Valid CreateCategoriaDto editCategoriaDto){

        return CategoriaDto.of(categoriaService.editarCategoria(editCategoriaDto, categoriaId));
    }

    @Operation(summary = "Elimina una categoria por su Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Categoria eliminado correctamente",
                    content = @Content),

    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable UUID id){
        categoriaService.eliminarCategoriaPorId(id);
        return ResponseEntity.noContent().build();

    }
}
