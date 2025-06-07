package com.salesianostriana.bioclick.controller;


import com.salesianostriana.bioclick.dto.PaginacionDto;
import com.salesianostriana.bioclick.dto.producto.CreateProductoDto;
import com.salesianostriana.bioclick.dto.producto.ProductoDto;
import com.salesianostriana.bioclick.model.Producto;
import com.salesianostriana.bioclick.model.User;
import com.salesianostriana.bioclick.service.ProductoService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/")
@Tag(name = "Producto", description = "El controlador de producto, para poder realizar todas las operaciones de gestión")

public class ProductoController {

    private final ProductoService productoService;

    @Operation(summary = "Crea un nuevo producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Producto registrado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                    {
                                                        "nombreProducto": "Auriculares inalámbricos sostenibles",
                                                        "descripcion": "Auriculares fabricados con materiales reciclados, de alta calidad y respetuosos con el medio ambiente.",
                                                        "imagenProducto": "https://ejemplo.com/imagenes/auriculares-sostenibles.jpg",
                                                        "precioProducto": 49.99,
                                                        "estado": "Reciclado"
                                                    }
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Solicitud incorrecta o datos inválidos",
                    content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<ProductoDto> createProducto(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Producto a crear", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateProductoDto.class),
                    examples = @ExampleObject(value = """
                                    {
                                      "nombreProducto": "Auriculares inalámbricos sostenibles",
                                      "descripcion": "Auriculares fabricados con materiales reciclados, de alta calidad y respetuosos con el medio ambiente.",
                                      "imagenProducto": "https://ejemplo.com/imagenes/auriculares-sostenibles.jpg",
                                      "precioProducto": 49.99,
                                      "fabricante": "EcoTech",
                                      "creadoPor": "550e8400-e29b-41d4-a716-446655440000",
                                      "estado": "Reciclado"
                                    }
                            """))) @RequestPart("crear") @Valid CreateProductoDto CreateProductoDto,
                                                      @RequestPart("file") MultipartFile file,
                                                      @AuthenticationPrincipal User user) {

        Producto p = productoService.createProducto(CreateProductoDto, user.getId(), file);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductoDto.of(p, getImageUrl(p.getImagenProducto())));
    }

    @Operation(summary = "Obtiene todos los productos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado productos",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "nombreProducto": "Cámara de Seguridad Reciclada",
                                                "descripcion": "Cámara de seguridad inteligente hecha con materiales reciclados.",
                                                "imagenProducto": "https://example.com/images/camara-seguridad.jpg",
                                                "precioProducto": 149.99,
                                                "estado": "Reacondicionado"
                                            }
                                """
                            )}
                    )}),
    })
    @GetMapping("/get/all")
    public PaginacionDto<ProductoDto> listarProductos(@PageableDefault(page=0, size=6) Pageable pageable) {

        return PaginacionDto.of(productoService.buscarProductos(pageable, false)
                .map(ProductoDto::of));
    }
    @Operation(summary = "Obtiene un producto por su Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Producto encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "id": "d290f1ee-6c54-4b01-90e6-d701748f0851",
                                                    "username": "johndoe",
                                                    "correo": "johndoe@gmail.com",
                                                    "fotoPerfil": "johndoe.jpg",
                                                    "fechaRegistro": "2023-01-10T09:30:00"
                                                }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ningún producto con ese ID",
                    content = @Content)
    })
    @GetMapping("/get/{productId}")
    public ProductoDto obtenerProducto(@PathVariable UUID productId) {
        return ProductoDto.of(productoService.buscarPorId(productId));
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
                                                    "nombreProducto": "Auriculares inalámbricos sostenibles",
                                                    "descripcion": "Auriculares fabricados con materiales reciclados, de alta calidad y respetuosos con el medio ambiente.",
                                                    "imagenProducto": "https://ejemplo.com/imagenes/auriculares-sostenibles.jpg",
                                                    "precioProducto": 19.99,
                                                    "estado": "Reacondicionado"
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
    @PutMapping("/edit/{productId}")
    public ProductoDto editarProducto(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Producto a editar", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductoDto.class),
                    examples = @ExampleObject(value = """
                                                        {
                                                          "nombreProducto": "Auriculares inalámbricos sostenibles",
                                                          "descripcion": "Auriculares fabricados con materiales reciclados, de alta calidad y respetuosos con el medio ambiente.",
                                                          "imagenProducto": "https://ejemplo.com/imagenes/auriculares-sostenibles.jpg",
                                                          "precioProducto": 19.99,
                                                          "fabricante": "EcoTech",
                                                          "estado": "Reacondicionado"
                                                        }
                            """))) @PathVariable UUID productId,
                                    @RequestPart("editar") @Valid CreateProductoDto editProductDto,
                                    @RequestPart("file") MultipartFile file,
                                    @AuthenticationPrincipal User user) {

        Producto p = productoService.editarProducto(editProductDto, productId, file, user.getId());

        return ProductoDto.of(p, getImageUrl(p.getImagenProducto()));
    }

    @Operation(summary = "Elimina un producto por su Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Producto eliminado correctamente",
                    content = @Content),

    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable UUID id){
        productoService.eliminarProductoPorId(id);
        return ResponseEntity.noContent().build();
    }

    public String getImageUrl(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();
    }
}
