package com.salesianostriana.bioclick.controller;


import com.salesianostriana.bioclick.dto.PaginacionDto;
import com.salesianostriana.bioclick.dto.producto.ProductoDto;
import com.salesianostriana.bioclick.dto.user.*;
import com.salesianostriana.bioclick.dto.VerificationCodeRequest.VerificationCodeRequest;
import com.salesianostriana.bioclick.model.User;
import com.salesianostriana.bioclick.security.jwt.access.JwtService;
import com.salesianostriana.bioclick.security.jwt.refresh.dto.RefreshTokenRequest;
import com.salesianostriana.bioclick.security.jwt.refresh.model.RefreshToken;
import com.salesianostriana.bioclick.security.jwt.refresh.service.RefreshTokenService;

import com.salesianostriana.bioclick.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Usuario", description = "El controlador de usuario, para poder realizar todas las operaciones de gestión")

public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    @Operation(summary = "Registra un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Usuario registrado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": "130b6531-b029-45bf-b973-9d9435a5aa15",
                                                "username": "luismi"
                                            }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Solicitud incorrecta o datos inválidos",
                    content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Contacto a registrar", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateUserRequest.class),
                    examples = @ExampleObject(value = """
                                                        {
                                                            "username": "luismi",
                                                            "password": "12345678",
                                                            "verifyPassword": "12345678",
                                                            "correo": "pruebadecuenta731@gmail.com"
                                                        }                                                       
                            """)))  @RequestPart("file") MultipartFile file,
                                    @RequestPart("register") @Valid CreateUserRequest createUserRequest) {

        User userRegistrado = userService.createUser(createUserRequest, file);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserDto.of(userRegistrado,getImageUrl(userRegistrado.getFotoPerfil())));
    }

    @Operation(summary = "Verifica un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Usuario verificado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": "130b6531-b029-45bf-b973-9d9435a5aa15",
                                                "username": "luismi",
                                                "correo": "pruebadecuenta731@gmail.com",
                                                "password": "{bcrypt}$2a$10$BXPNNiTCZPV5PKUDyogKDucZOD6UsVAzXq8VEU2w/jjr5Tn1SFtx.",
                                                "fotoPerfil": null,
                                                "fechaRegistro": "2025-02-21T09:23:01.327146",
                                                "enabled": true,
                                                "verificationCode": "037978",
                                                "authorities": [
                                                    {
                                                        "authority": "ROLE_USUARIO"
                                                    }
                                                ],
                                                "accountNonLocked": true,
                                                "accountNonExpired": true,
                                                "credentialsNonExpired": true
                                            }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Solicitud incorrecta o datos inválidos",
                    content = @Content)
    })
    @PostMapping("/auth/verify")
    public ResponseEntity<?> verify(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Contacto a verificar", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = VerificationCodeRequest.class),
                    examples = @ExampleObject(value = """
                                                        {
                                                            "code": 123456
                                                        }
                            """))) @RequestBody @Valid VerificationCodeRequest verificationTokenRequest) {
        User user = userService.verifyUser(verificationTokenRequest);
        return ResponseEntity.ok(user);
    }


    @Operation(summary = "Loguea un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Usuario verificado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": "130b6531-b029-45bf-b973-9d9435a5aa15",
                                                "username": "luismi",
                                                "correo": "pruebadecuenta731@gmail.com",
                                                "password": "{bcrypt}$2a$10$BXPNNiTCZPV5PKUDyogKDucZOD6UsVAzXq8VEU2w/jjr5Tn1SFtx.",
                                                "fotoPerfil": null,
                                                "fechaRegistro": "2025-02-21T09:23:01.327146",
                                                "enabled": true,
                                                "verificationCode": "037978",
                                                "authorities": [
                                                    {
                                                        "authority": "ROLE_USUARIO"
                                                    }
                                                ],
                                                "accountNonLocked": true,
                                                "accountNonExpired": true,
                                                "credentialsNonExpired": true
                                            }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Solicitud incorrecta o datos inválidos",
                    content = @Content)
    })
    @PostMapping("/login")
    @PostAuthorize("returnObject.body.enabled == true")
    public ResponseEntity<UserResponse> login(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Contacto a loguear", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoginRequest.class),
                    examples = @ExampleObject(value = """
                                                        {
                                                            "username": "luismi",
                                                            "password": "12345678"
                                                        }
                            """))) @RequestBody LoginRequest loginRequest) {




        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.username(),
                                loginRequest.password()
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();


        String accessToken = jwtService.generateAccessToken(user);


        RefreshToken refreshToken = refreshTokenService.create(user);

        System.out.println(user.getAuthorities());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user, accessToken, refreshToken.getToken()));

    }

    @PostMapping("/auth/refresh/token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest req) {
        String token = req.refreshToken();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(refreshTokenService.refreshToken(token));

    }

    @Operation(summary = "Actualiza el usuario que realiza la petición")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Usuario actualizado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": "dd4e888f-374d-4a1d-9b11-df68f68c9876",
                                                "username": "miguelCampos",
                                                "correo": "pruebadecuenta731@gmail.com",
                                                "fotoPerfil": null,
                                                "fechaRegistro": "2023-08-21T10:30:00"
                                            }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para la actualización",
                    content = @Content)
    })
    @PutMapping("/edit/me")
    public UserDto editarUsuario(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Usuario a editar", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class),
                    examples = @ExampleObject(value = """
                                        {
                                            "username": "miguelCampos",
                                            "password": "animo",
                                            "verifyPassword": "animo",
                                            "correo": "pruebadecuenta731@gmail.com"
                                        }
                            """))) @AuthenticationPrincipal User user,
                                 @RequestPart("edit") @Valid EditUserDto editUserDto,
                                 @RequestPart("file")MultipartFile file) {

        User userEditado = userService.editarUsuario(user, editUserDto, file);

        return UserDto.of(userEditado, getImageUrl(userEditado.getFotoPerfil()));
    }

    @Operation(summary = "Busca productos cuyo precio esté entre dos valores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Productos encontrados correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "numPagina": 0,
                                                    "tamanioPagina": 10,
                                                    "elementosEncontrados": 5,
                                                    "paginasTotales": 1,
                                                    "contenido": [
                                                        {
                                                            "nombreProducto": "Teclado Mecánico Eco",
                                                            "descripcion": "Teclado hecho con plásticos reciclados y switches sostenibles.",
                                                            "imagenProducto": "https://example.com/images/teclado-mecanico-eco.jpg",
                                                            "precioProducto": 69.99,
                                                            "estado": "Reciclado"
                                                        },
                                                        {
                                                            "nombreProducto": "Ratón Inalámbrico Sostenible",
                                                            "descripcion": "Ratón hecho con componentes reciclados y baterías de larga duración.",
                                                            "imagenProducto": "https://example.com/images/raton-sostenible.jpg",
                                                            "precioProducto": 39.99,
                                                            "estado": "Reciclado"
                                                        },
                                                        {
                                                            "nombreProducto": "Altavoz Bluetooth Eco",
                                                            "descripcion": "Altavoz con carcasa de madera reciclada y sonido envolvente.",
                                                            "imagenProducto": "https://example.com/images/altavoz-eco.jpg",
                                                            "precioProducto": 59.99,
                                                            "estado": "Reacondicionado"
                                                        },
                                                        {
                                                            "nombreProducto": "Auriculares de Alta Fidelidad",
                                                            "descripcion": "Auriculares con cancelación de ruido, hechos con materiales reciclados.",
                                                            "imagenProducto": "https://example.com/images/auriculares-alta-fidelidad.jpg",
                                                            "precioProducto": 89.99,
                                                            "estado": "Reacondicionado"
                                                        },
                                                        {
                                                            "nombreProducto": "Cargador Solar Portátil",
                                                            "descripcion": "Cargador portátil que funciona con energía solar.",
                                                            "imagenProducto": "https://example.com/images/cargador-solar.jpg",
                                                            "precioProducto": 29.99,
                                                            "estado": "Reacondicionado"
                                                        }
                                                    ]
                                                }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para la búsqueda",
                    content = @Content)
    })
    @GetMapping("/productos/get/precio-entre")
    public PaginacionDto<ProductoDto> buscarProductoPorPrecioEntreMedio(
            @RequestParam(value = "min", required = false) Double min,
            @RequestParam(value = "max", required = false) Double max,
            @PageableDefault(size = 10) Pageable pageable) {

        return PaginacionDto.of(userService.buscarProductoPorPrecioEntreMedio(min, max, pageable, false)
                .map(ProductoDto::of));
    }

    @Operation(summary = "Busca productos cuyo precio sea mayor que un valor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Productos encontrados correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "numPagina": 0,
                                                "tamanioPagina": 10,
                                                "elementosEncontrados": 7,
                                                "paginasTotales": 1,
                                                "contenido": [
                                                    {
                                                        "nombreProducto": "Teclado Mecánico Eco",
                                                        "descripcion": "Teclado hecho con plásticos reciclados y switches sostenibles.",
                                                        "imagenProducto": "https://example.com/images/teclado-mecanico-eco.jpg",
                                                        "precioProducto": 69.99,
                                                        "estado": "Reciclado"
                                                    },
                                                    {
                                                        "nombreProducto": "Altavoz Bluetooth Eco",
                                                        "descripcion": "Altavoz con carcasa de madera reciclada y sonido envolvente.",
                                                        "imagenProducto": "https://example.com/images/altavoz-eco.jpg",
                                                        "precioProducto": 59.99,
                                                        "estado": "Reacondicionado"
                                                    },
                                                    {
                                                        "nombreProducto": "Smartphone Eco-Friendly",
                                                        "descripcion": "Smartphone de bajo consumo, fabricado con materiales reciclados.",
                                                        "imagenProducto": "https://example.com/images/smartphone-eco.jpg",
                                                        "precioProducto": 499.99,
                                                        "estado": "Reciclado"
                                                    },
                                                    {
                                                        "nombreProducto": "Tablet Reciclada",
                                                        "descripcion": "Tablet de alta calidad fabricada con materiales reciclados y eficiencia energética.",
                                                        "imagenProducto": "https://example.com/images/tablet-reciclada.jpg",
                                                        "precioProducto": 199.99,
                                                        "estado": "Reciclado"
                                                    },
                                                    {
                                                        "nombreProducto": "Auriculares de Alta Fidelidad",
                                                        "descripcion": "Auriculares con cancelación de ruido, hechos con materiales reciclados.",
                                                        "imagenProducto": "https://example.com/images/auriculares-alta-fidelidad.jpg",
                                                        "precioProducto": 89.99,
                                                        "estado": "Reacondicionado"
                                                    },
                                                    {
                                                        "nombreProducto": "Monitor 4K Sostenible",
                                                        "descripcion": "Monitor con panel reciclado y bajo consumo energético.",
                                                        "imagenProducto": "https://example.com/images/monitor-4k-sostenible.jpg",
                                                        "precioProducto": 299.99,
                                                        "estado": "Reciclado"
                                                    },
                                                    {
                                                        "nombreProducto": "Cámara de Seguridad Reciclada",
                                                        "descripcion": "Cámara de seguridad inteligente hecha con materiales reciclados.",
                                                        "imagenProducto": "https://example.com/images/camara-seguridad.jpg",
                                                        "precioProducto": 149.99,
                                                        "estado": "Reacondicionado"
                                                    }
                                                ]
                                            }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para la búsqueda",
                    content = @Content)
    })
    @GetMapping("/productos/get/precio-mayor")
    public PaginacionDto<ProductoDto> buscarProductoPorPrecioMayor(
            @RequestParam(value = "min", required = false) Double min,
            @PageableDefault(size = 10) Pageable pageable) {

        return PaginacionDto.of(userService.buscarProductoPorPrecioMayor(min, pageable, false)
                .map(ProductoDto::of));
    }

    @Operation(summary = "Busca productos cuyo precio sea menor que un valor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Productos encontrados correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                        {
                                            "numPagina": 0,
                                            "tamanioPagina": 10,
                                            "elementosEncontrados": 5,
                                            "paginasTotales": 1,
                                            "contenido": [
                                                {
                                                    "nombreProducto": "Teclado Mecánico Eco",
                                                    "descripcion": "Teclado hecho con plásticos reciclados y switches sostenibles.",
                                                    "imagenProducto": "https://example.com/images/teclado-mecanico-eco.jpg",
                                                    "precioProducto": 69.99,
                                                    "estado": "Reciclado"
                                                },
                                                {
                                                    "nombreProducto": "Ratón Inalámbrico Sostenible",
                                                    "descripcion": "Ratón hecho con componentes reciclados y baterías de larga duración.",
                                                    "imagenProducto": "https://example.com/images/raton-sostenible.jpg",
                                                    "precioProducto": 39.99,
                                                    "estado": "Reciclado"
                                                },
                                                {
                                                    "nombreProducto": "Altavoz Bluetooth Eco",
                                                    "descripcion": "Altavoz con carcasa de madera reciclada y sonido envolvente.",
                                                    "imagenProducto": "https://example.com/images/altavoz-eco.jpg",
                                                    "precioProducto": 59.99,
                                                    "estado": "Reacondicionado"
                                                },
                                                {
                                                    "nombreProducto": "Lámpara LED Reciclada",
                                                    "descripcion": "Lámpara LED fabricada con plástico reciclado y de bajo consumo.",
                                                    "imagenProducto": "https://example.com/images/lampara-led.jpg",
                                                    "precioProducto": 19.99,
                                                    "estado": "Reciclado"
                                                },
                                                {
                                                    "nombreProducto": "Cargador Solar Portátil",
                                                    "descripcion": "Cargador portátil que funciona con energía solar.",
                                                    "imagenProducto": "https://example.com/images/cargador-solar.jpg",
                                                    "precioProducto": 29.99,
                                                    "estado": "Reacondicionado"
                                                }
                                            ]
                                        }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para la búsqueda",
                    content = @Content)
    })
    @GetMapping("/productos/get/precio-menor")
    public PaginacionDto<ProductoDto> buscarProductoPorPrecioMenor(
            @RequestParam(value = "max", required = false) Double max,
            @PageableDefault(size = 10) Pageable pageable) {

        return PaginacionDto.of(userService.buscarProductoPorPrecioMenor(max, pageable, false)
                .map(ProductoDto::of));
    }

    @Operation(summary = "Busca productos por nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Productos encontrados correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "numPagina": 0,
                                                    "tamanioPagina": 10,
                                                    "elementosEncontrados": 3,
                                                    "paginasTotales": 1,
                                                    "contenido": [
                                                        {
                                                            "nombreProducto": "Teclado Mecánico Eco",
                                                            "descripcion": "Teclado hecho con plásticos reciclados y switches sostenibles.",
                                                            "imagenProducto": "https://example.com/images/teclado-mecanico-eco.jpg",
                                                            "precioProducto": 69.99,
                                                            "estado": "Reciclado"
                                                        },
                                                        {
                                                            "nombreProducto": "Altavoz Bluetooth Eco",
                                                            "descripcion": "Altavoz con carcasa de madera reciclada y sonido envolvente.",
                                                            "imagenProducto": "https://example.com/images/altavoz-eco.jpg",
                                                            "precioProducto": 59.99,
                                                            "estado": "Reacondicionado"
                                                        },
                                                        {
                                                            "nombreProducto": "Smartphone Eco-Friendly",
                                                            "descripcion": "Smartphone de bajo consumo, fabricado con materiales reciclados.",
                                                            "imagenProducto": "https://example.com/images/smartphone-eco.jpg",
                                                            "precioProducto": 499.99,
                                                            "estado": "Reciclado"
                                                        }
                                                    ]
                                                }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para la búsqueda",
                    content = @Content)
    })
    @GetMapping("/productos/get/nombre")
    public PaginacionDto<ProductoDto> buscarProductoPorNombre(
            @RequestParam(value = "nombre", required = false) String nombre,
            @PageableDefault(size = 10) Pageable pageable) {

        return PaginacionDto.of(userService.buscarProductoPorNombre(nombre, pageable, false)
                .map(ProductoDto::of));
    }



    @Operation(summary = "Obtiene al usuario que realiza la petición")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Usuario encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "id": "dd4e888f-374d-4a1d-9b11-df68f68c9876",
                                                    "username": "jamesmoore",
                                                    "correo": "james.moore@yahoo.com",
                                                    "fotoPerfil": "jamesmoore.png",
                                                    "fechaRegistro": "2023-08-21T10:30:00"
                                                }
                                """
                            )}
                    )})
    })
    @GetMapping("/get/me")
    public UserDto obtenerUsuario(@AuthenticationPrincipal User user) {
        return UserDto.of(user, getImageUrl(user.getFotoPerfil()));
    }

    @Operation(summary = "Elimina un usuario por su Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Usuario eliminado correctamente",
                    content = @Content),

    })
    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable UUID id){
        userService.eliminarUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    public String getImageUrl(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();
    }
}
