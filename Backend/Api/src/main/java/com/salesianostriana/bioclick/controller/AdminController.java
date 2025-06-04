package com.salesianostriana.bioclick.controller;

import com.salesianostriana.bioclick.dto.CambiarRolRequest;
import com.salesianostriana.bioclick.dto.PaginacionDto;
import com.salesianostriana.bioclick.dto.VerificationCodeRequest.VerificationCodeRequest;
import com.salesianostriana.bioclick.dto.admin.AdminDto;
import com.salesianostriana.bioclick.dto.admin.CreateAdminRequest;
import com.salesianostriana.bioclick.dto.admin.EditAdminDto;
import com.salesianostriana.bioclick.dto.manager.EditManagerDto;
import com.salesianostriana.bioclick.dto.manager.ManagerDto;
import com.salesianostriana.bioclick.dto.user.EditUserDto;
import com.salesianostriana.bioclick.dto.user.UserDto;
import com.salesianostriana.bioclick.model.Admin;
import com.salesianostriana.bioclick.model.Manager;
import com.salesianostriana.bioclick.model.User;
import com.salesianostriana.bioclick.service.AdminService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/")
@Tag(name = "Admin", description = "El controlador de admin, para poder realizar todas las operaciones de gestión")
public class AdminController {

    private final AdminService adminService;
    private final ManagerService managerService;

    @Operation(summary = "Crea un nuevo administrador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Administrador registrado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = {@ExampleObject(
                                    value = """
                                                    {
                                                        "username": "luismiAdmin",
                                                        "correo": "pruebadecuenta731@gmail.com",
                                                        "fotoPerfil": "https://robohash.org/luismiAdmin",
                                                        "ultimaAccion": "None",
                                                        "fechaUltimaAccion": "2025-02-22T13:08:50.1754001"
                                                    }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Solicitud incorrecta o datos inválidos",
                    content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<AdminDto> createAdmin(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Contacto a registrar", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateAdminRequest.class),
                    examples = @ExampleObject(value = """
                                                    {
                                                        "username": "luismiAdmin",
                   
                                                       "password": "12345678",
                                                        "verifyPassword": "12345678",
                                                        "correo": "pruebadecuenta731@gmail.com"
                                                    }
                            """))) @RequestPart("crear") @Valid CreateAdminRequest createAdminRequest,
                                    @RequestPart("file") MultipartFile file,
                                    @AuthenticationPrincipal Admin admin) {
        Admin adminCreado =adminService.createAdmin(createAdminRequest, admin.getId(), file);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AdminDto.of(adminCreado,getImageUrl(adminCreado.getFotoPerfil())));
    }

    @Operation(summary = "Verifica un admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Admin verificado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdminDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "username": "luismiAdmin",
                                                "correo": "pruebadecuenta731@gmail.com",
                                                "fotoPerfil": "https://robohash.org/luismiAdmin",
                                                "ultimaAccion": "None",
                                                "fechaUltimaAccion": "2025-02-22T13:08:50.1754001"
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
            description = "Admin a verificar", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = VerificationCodeRequest.class),
                    examples = @ExampleObject(value = """
                                                        {
                                                            "code": "080404"
                                                        }
                            """))) @RequestBody VerificationCodeRequest verificationCodeRequest) {
        return ResponseEntity.ok(AdminDto.of(adminService.verifyAdmin(verificationCodeRequest)));
    }
    @Operation(summary = "Actualiza un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Usuario actualizado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "username": "luismiSigueSiendoAdmin",
                                                    "correo": "patricia.jones5@gmail.com",
                                                    "fotoPerfil": null,
                                                    "ultimaAccion": "None",
                                                    "fechaUltimaAccion": "2025-02-22T15:47:41.6212467"
                                                }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ningún usuario con ese ID",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para la actualización",
                    content = @Content)
    })
    @PutMapping("/edit/admin/{adminId}")
    public AdminDto editarAdmin(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Admin a editar", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdminDto.class),
                    examples = @ExampleObject(value = """
                            {
                                "username": "luismiSigueSiendoAdmin",
                                "password": "12345678",
                                "verifyPassword": "12345678",
                                "correo": "patricia.jones5@gmail.com"
                            }
                            """))) @PathVariable UUID adminId,
                                @RequestPart("editar") @Valid EditAdminDto editAdminDto,
                                @RequestPart("file") MultipartFile file){

        Admin admin = adminService.editarAdmin(adminId, editAdminDto, file);

        return AdminDto.of(admin, getImageUrl(admin.getFotoPerfil()));
    }

    @Operation(summary = "Actualiza el admin que realiza la petición")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Admin actualizado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdminDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "username": "patriciajonesAdmin",
                                                    "correo": "patricia.jones@gmail.com",
                                                    "fotoPerfil": null,
                                                    "ultimaAccion": "Borrado de usuario",
                                                    "fechaUltimaAccion": "2025-02-22T15:47:58.4207571"
                                                }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para la actualización",
                    content = @Content)
    })
    @PutMapping("/edit/me")
    public AdminDto editarEsteAdmin(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Admin a editar", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdminDto.class),
                    examples = @ExampleObject(value = """
                                                            {
                                                                "username": "patriciajonesAdmin",
                                                                "password": "12345678",
                                                                "verifyPassword": "12345678",
                                                                "correo": "patricia.jones@gmail.com"
                                                            }
                            """))) @AuthenticationPrincipal Admin admin,
                                    @RequestPart("editar") @Valid EditAdminDto editAdminDto,
                                    @RequestPart("file") MultipartFile file) {

        Admin adminEditado = adminService.editarEsteAdmin(admin, editAdminDto,file);

        return AdminDto.of(adminEditado, getImageUrl(adminEditado.getFotoPerfil()));
    }

    @Operation(summary = "Obtiene todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado usuarios",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "content": [
                                                    {
                                                        "username": "patriciajones",
                                                        "correo": "patricia.jones@gmail.com",
                                                        "fotoPerfil": "patriciajones.jpg",
                                                        "ultimaAccion": "Borrado de usuario",
                                                        "fechaUltimaAccion": "2025-02-22T16:38:12.7639036"
                                                    },
                                                    {
                                                        "username": "jamesmoore",
                                                        "correo": "james.moore@yahoo.com",
                                                        "fotoPerfil": "jamesmoore.png",
                                                        "ultimaAccion": "Borrado de usuario",
                                                        "fechaUltimaAccion": "2025-02-22T16:38:12.7639036"
                                                    }
                                                ],
                                                "pageable": {
                                                    "pageNumber": 0,
                                                    "pageSize": 4,
                                                    "sort": {
                                                        "empty": true,
                                                        "sorted": false,
                                                        "unsorted": true
                                                    },
                                                    "offset": 0,
                                                    "paged": true,
                                                    "unpaged": false
                                                },
                                                "last": true,
                                                "totalPages": 1,
                                                "totalElements": 2,
                                                "first": true,
                                                "size": 4,
                                                "number": 0,
                                                "sort": {
                                                    "empty": true,
                                                    "sorted": false,
                                                    "unsorted": true
                                                },
                                                "numberOfElements": 2,
                                                "empty": false
                                            }
                                """
                            )}
                    )}),
    })
    @GetMapping("/get/users")
    public PaginacionDto<UserDto> listarUsers(@PageableDefault(page=0, size=12) Pageable pageable, @AuthenticationPrincipal Admin admin) {

        return PaginacionDto.of(adminService.buscarUsuarios(pageable, admin.getId(), false)
                .map(usuario -> UserDto.of(usuario, getImageUrl(usuario.getFotoPerfil()))));
    }

    @Operation(summary = "Obtiene todos los administradores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado administradores",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdminDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "content": [
                                                    {
                                                        "username": "patriciajones",
                                                        "correo": "patricia.jones@gmail.com",
                                                        "fotoPerfil": "patriciajones.jpg",
                                                        "ultimaAccion": "Borrado de usuario",
                                                        "fechaUltimaAccion": "2025-02-22T16:38:12.7639036"
                                                    },
                                                    {
                                                        "username": "jamesmoore",
                                                        "correo": "james.moore@yahoo.com",
                                                        "fotoPerfil": "jamesmoore.png",
                                                        "ultimaAccion": "Borrado de usuario",
                                                        "fechaUltimaAccion": "2025-02-22T16:38:12.7639036"
                                                    }
                                                ],
                                                "pageable": {
                                                    "pageNumber": 0,
                                                    "pageSize": 4,
                                                    "sort": {
                                                        "empty": true,
                                                        "sorted": false,
                                                        "unsorted": true
                                                    },
                                                    "offset": 0,
                                                    "paged": true,
                                                    "unpaged": false
                                                },
                                                "last": true,
                                                "totalPages": 1,
                                                "totalElements": 2,
                                                "first": true,
                                                "size": 4,
                                                "number": 0,
                                                "sort": {
                                                    "empty": true,
                                                    "sorted": false,
                                                    "unsorted": true
                                                },
                                                "numberOfElements": 2,
                                                "empty": false
                                            }
                                """
                            )}
                    )}),
    })
    @GetMapping("/get/admins")
    public PaginacionDto<AdminDto> listarAdmins(@PageableDefault(page=0, size=4) Pageable pageable, @AuthenticationPrincipal Admin admin) {

        return PaginacionDto.of(adminService.buscarAdmins(pageable, admin.getId())
                .map(AdminDto::of));
    }

    @Operation(summary = "Obtiene todos los managers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado managers",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdminDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "content": [
                                                    {
                                                        "username": "patriciajones",
                                                        "correo": "patricia.jones@gmail.com",
                                                        "fotoPerfil": "patriciajones.jpg",
                                                        "ultimaAccion": "Borrado de usuario",
                                                        "fechaUltimaAccion": "2025-02-22T16:38:12.7639036"
                                                    },
                                                    {
                                                        "username": "jamesmoore",
                                                        "correo": "james.moore@yahoo.com",
                                                        "fotoPerfil": "jamesmoore.png",
                                                        "ultimaAccion": "Borrado de usuario",
                                                        "fechaUltimaAccion": "2025-02-22T16:38:12.7639036"
                                                    }
                                                ],
                                                "pageable": {
                                                    "pageNumber": 0,
                                                    "pageSize": 4,
                                                    "sort": {
                                                        "empty": true,
                                                        "sorted": false,
                                                        "unsorted": true
                                                    },
                                                    "offset": 0,
                                                    "paged": true,
                                                    "unpaged": false
                                                },
                                                "last": true,
                                                "totalPages": 1,
                                                "totalElements": 2,
                                                "first": true,
                                                "size": 4,
                                                "number": 0,
                                                "sort": {
                                                    "empty": true,
                                                    "sorted": false,
                                                    "unsorted": true
                                                },
                                                "numberOfElements": 2,
                                                "empty": false
                                            }
                                """
                            )}
                    )}),
    })
    @GetMapping("/get/managers")
    public PaginacionDto<ManagerDto> listarManagers(@PageableDefault(page=0, size=4) Pageable pageable, @AuthenticationPrincipal Admin admin) {

        return PaginacionDto.of(adminService.buscarManagers(pageable, admin.getId())
                .map(manager -> ManagerDto.of(manager, getImageUrl(manager.getFotoPerfil()))));
    }

    @Operation(summary = "Obtiene un manager por su Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Manager encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ManagerDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                        {
                                            "username": "emilywatson",
                                            "correo": "emily.watson@example.com",
                                            "fotoPerfilUrl": "http://localhost:8080/download/emilywatson.jpg",
                                            "ultimaAccion": "Actualización de producto",
                                            "fechaUltimaAccion": "2024-02-18T16:30:00"
                                        }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ningún manager con ese ID",
                    content = @Content)
    })
    @GetMapping("/get/manager/{managerId}")
    public ManagerDto obtenerManager(@PathVariable UUID managerId) {

        Manager manager = adminService.buscarManagerPorId(managerId);
        return ManagerDto.of(manager, getImageUrl(manager.getFotoPerfil()));
    }


    @Operation(summary = "Actualiza un Manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Manager actualizado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ManagerDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "username": "watsonEdit",
                                                "correo": "correoCambiadoAdmin@example.com",
                                                "fotoPerfilUrl": "http://localhost:8080/download/106b7ef1-adf7-4db9-b097-e5e08b21368e.jpg",
                                                "ultimaAccion": "Actualización de producto",
                                                "fechaUltimaAccion": "2024-02-18T16:30:00"
                                            }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para la actualización",
                    content = @Content)
    })
    @PutMapping("/edit/manager/{managerId}")
    public ResponseEntity<ManagerDto> editManager(@io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                          description = "Producto a crear", required = true,
                                                          content = @Content(mediaType = "application/json",
                                                                  schema = @Schema(implementation = ManagerDto.class),
                                                                  examples = @ExampleObject(value = """
                                                            {
                                                                "username": "watsonEdit",
                                                                "correo": "correoCambiadoAdmin@example.com",
                                                                "fotoPerfilUrl": "http://localhost:8080/download/DiagramaBioClick.drawio.png",
                                                                "ultimaAccion": "Actualización de producto",
                                                                "fechaUltimaAccion": "2024-02-18T16:30:00"
                                                            }
                            """))) @RequestPart("editar") @Valid EditManagerDto editManagerDto,
                                                  @PathVariable UUID managerId,
                                                  @RequestPart("file") MultipartFile file,
                                                  @AuthenticationPrincipal User user) {

        Manager managerEditado = managerService.editaManager(user.getId(),managerId ,editManagerDto,file);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ManagerDto.of(managerEditado, getImageUrl(managerEditado.getFotoPerfil())));
    }

    @Operation(summary = "Obtiene un usuario por su Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Usuario encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
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
                    description = "No se ha encontrado ningún usuario con ese ID",
                    content = @Content)
    })
    @GetMapping("/get/{userId}")
    public UserDto obtenerUsuario(@PathVariable UUID userId) {

        User user = adminService.buscarPorId(userId);
        return UserDto.of(user, getImageUrl(user.getFotoPerfil()));
    }

    @Operation(summary = "Actualiza un usuario existente")
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
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ningún usuario con ese ID",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para la actualización",
                    content = @Content)
    })
    @PutMapping("/edit/user/{userId}")
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
                            """))) @PathVariable UUID userId,
                                 @RequestPart("edit") @Valid EditUserDto editUserDto,
                                 @RequestPart("file") MultipartFile file,
                                 @AuthenticationPrincipal Admin admin){

        User userEditado = adminService.editarUsuario(editUserDto, userId, admin.getId(), file);

        return UserDto.of(userEditado, getImageUrl(userEditado.getFotoPerfil()));
    }
    @Operation(summary = "Actualiza el rol de un usuario o admin existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Usuario actualizado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "username": "lisawilliams",
                                                "correo": "lisa.williams@icloud.com",
                                                "fotoPerfil": "lisawilliams.jpg",
                                                "fechaRegistro": "2023-07-19T14:00:00"
                                            }
                                """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ningún usuario con ese ID",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para la actualización",
                    content = @Content)
    })
    @PutMapping("/cambiar-rol/{userId}")
    public ResponseEntity<UserDto> cambiarRol(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Usuario a editar", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class),
                    examples = @ExampleObject(value = """
                                                {
                                                    "nuevoRol": "ROLE_ADMIN"
                                                }
                            """))) @PathVariable UUID userId,
                                  @RequestBody @Valid CambiarRolRequest nuevoRol,
                                  @AuthenticationPrincipal Admin admin) {

        User userEditado = adminService.cambiarRol(userId, admin.getId(), nuevoRol);

        return ResponseEntity.ok(UserDto.of(userEditado,getImageUrl(userEditado.getFotoPerfil())));
    }

    @Operation(summary = "Buscar usuarios por nombrenombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Usuarios encontrados correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "numPagina": 0,
                                                "tamanioPagina": 10,
                                                "elementosEncontrados": 5,
                                                "paginasTotales": 1,
                                                "contenido": [
                                                    {
                                                        "username": "johndoe",
                                                        "correo": "johndoe@gmail.com",
                                                        "fotoPerfil": "johndoe.jpg",
                                                        "fechaRegistro": "2023-01-10T09:30:00"
                                                    },
                                                    {
                                                        "username": "janedoe",
                                                        "correo": "janedoe@yahoo.com",
                                                        "fotoPerfil": "janedoe.jpg",
                                                        "fechaRegistro": "2023-01-15T14:45:00"
                                                    },
                                                    {
                                                        "username": "emilyjohnson",
                                                        "correo": "emily.johnson@outlook.com",
                                                        "fotoPerfil": "emilyjohnson.png",
                                                        "fechaRegistro": "2023-03-25T16:20:00"
                                                    },
                                                    {
                                                        "username": "jamesmoore",
                                                        "correo": "james.moore@yahoo.com",
                                                        "fotoPerfil": "jamesmoore.png",
                                                        "fechaRegistro": "2023-08-21T10:30:00"
                                                    },
                                                    {
                                                        "username": "patriciajones",
                                                        "correo": "patricia.jones@gmail.com",
                                                        "fotoPerfil": "patriciajones.jpg",
                                                        "fechaRegistro": "2023-09-29T09:45:00"
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
    @GetMapping("/get/users-by-name")
    public ResponseEntity<PaginacionDto<UserDto>> buscarUsuariosPorNombre(
            @RequestParam(value = "nombre", required = false) String nombreUsuario,
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal Admin admin) {

        Page<UserDto> users = adminService.buscarUserPorNombreUsuario(nombreUsuario, admin.getId(), pageable, false)
                .map(usuario -> UserDto.of(usuario, getImageUrl(usuario.getFotoPerfil())));

        return ResponseEntity.ok(PaginacionDto.of(users));
    }

    @Operation(summary = "Buscar usuarios por email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Usuarios encontrados correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "numPagina": 0,
                                                "tamanioPagina": 10,
                                                "elementosEncontrados": 5,
                                                "paginasTotales": 1,
                                                "contenido": [
                                                    {
                                                        "username": "johndoe",
                                                        "correo": "johndoe@gmail.com",
                                                        "fotoPerfilUrl": "http://localhost:8080/download/johndoe.jpg",
                                                        "fechaRegistro": "2023-01-10:09-30-00"
                                                    },
                                                    {
                                                        "username": "janedoe",
                                                        "correo": "janedoe@yahoo.com",
                                                        "fotoPerfilUrl": "http://localhost:8080/download/janedoe.jpg",
                                                        "fechaRegistro": "2023-01-15:14-45-00"
                                                    },
                                                    {
                                                        "username": "emilyjohnson",
                                                        "correo": "emily.johnson@outlook.com",
                                                        "fotoPerfilUrl": "http://localhost:8080/download/emilyjohnson.png",
                                                        "fechaRegistro": "2023-03-25:16-20-00"
                                                    },
                                                    {
                                                        "username": "jamesmoore",
                                                        "correo": "james.moore@yahoo.com",
                                                        "fotoPerfilUrl": "http://localhost:8080/download/jamesmoore.png",
                                                        "fechaRegistro": "2023-08-21:10-30-00"
                                                    },
                                                    {
                                                        "username": "patriciajones",
                                                        "correo": "patricia.jones@gmail.com",
                                                        "fotoPerfilUrl": "http://localhost:8080/download/patriciajones.jpg",
                                                        "fechaRegistro": "2023-09-29:09-45-00"
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
    @GetMapping("/get/users-by-email")
    public ResponseEntity<PaginacionDto<UserDto>> buscarUsuariosPorCorreo(
            @RequestParam(value = "email", required = false) String correo,
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal Admin admin) {

        Page<UserDto> users = adminService.buscarUserPorCorreo(correo, admin.getId(), pageable, false)
                .map(usuario -> UserDto.of(usuario, getImageUrl(usuario.getFotoPerfil())));


        return ResponseEntity.ok(PaginacionDto.of(users));
    }
    @Operation(summary = "Buscar usuarios por entre dos fechas dadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Usuarios encontrados correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "numPagina": 0,
                                                    "tamanioPagina": 10,
                                                    "elementosEncontrados": 1,
                                                    "paginasTotales": 1,
                                                    "contenido": [
                                                        {
                                                            "username": "johndoe",
                                                            "correo": "johndoe@gmail.com",
                                                            "fotoPerfil": "johndoe.jpg",
                                                            "fechaRegistro": "2023-01-10:09-30-00"
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
    @GetMapping("/get/users-between-dates")
    public ResponseEntity<PaginacionDto<UserDto>> buscarUsuariosEntreFechas(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate fechaInicio,
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate fechaFinal,
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal Admin admin) {

        Page<UserDto> users = adminService.buscarUserEntreFechas(fechaInicio, admin.getId(), fechaFinal, pageable, false)
                .map(usuario -> UserDto.of(usuario, getImageUrl(usuario.getFotoPerfil())));


        return ResponseEntity.ok(PaginacionDto.of(users));
    }
    @Operation(summary = "Buscar usuarios por fechas anterior a la dada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Usuarios encontrados correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                                {
                                                    "numPagina": 0,
                                                    "tamanioPagina": 10,
                                                    "elementosEncontrados": 4,
                                                    "paginasTotales": 1,
                                                    "contenido": [
                                                        {
                                                            "username": "johndoe",
                                                            "correo": "johndoe@gmail.com",
                                                            "fotoPerfil": "johndoe.jpg",
                                                            "fechaRegistro": "2023-01-10:09-30-00"
                                                        },
                                                        {
                                                            "username": "janedoe",
                                                            "correo": "janedoe@yahoo.com",
                                                            "fotoPerfil": "janedoe.jpg",
                                                            "fechaRegistro": "2023-01-15:14-45-00"
                                                        },
                                                        {
                                                            "username": "alexsmith",
                                                            "correo": "alexsmith@hotmail.com",
                                                            "fotoPerfil": "alexsmith.png",
                                                            "fechaRegistro": "2023-02-20:11-15-00"
                                                        },
                                                        {
                                                            "username": "emilyjohnson",
                                                            "correo": "emily.johnson@outlook.com",
                                                            "fotoPerfil": "emilyjohnson.png",
                                                            "fechaRegistro": "2023-03-25:16-20-00"
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
    @GetMapping("/get/users-before-dates")
    public ResponseEntity<PaginacionDto<UserDto>> buscarUsuariosAntesDe(
            @RequestParam(value = "fechaFinal", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFinal,
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal Admin admin) {

        Page<UserDto> users = adminService.buscarUserPorRegistroMenor(fechaFinal, admin.getId(), pageable, false)
                .map(usuario -> UserDto.of(usuario, getImageUrl(usuario.getFotoPerfil())));


        return ResponseEntity.ok(PaginacionDto.of(users));
    }
    @Operation(summary = "Buscar usuarios por fechas posterior a la dada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Usuarios encontrados correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "numPagina": 0,
                                                "tamanioPagina": 10,
                                                "elementosEncontrados": 5,
                                                "paginasTotales": 1,
                                                "contenido": [
                                                    {
                                                        "username": "sarawilson",
                                                        "correo": "sara.wilson@live.com",
                                                        "fotoPerfil": "sarawilson.jpg",
                                                        "fechaRegistro": "2023-05-08:08-45-00"
                                                    },
                                                    {
                                                        "username": "davidlee",
                                                        "correo": "david.lee@aol.com",
                                                        "fotoPerfil": "davidlee.png",
                                                        "fechaRegistro": "2023-06-12:12-10-00"
                                                    },
                                                    {
                                                        "username": "lisawilliams",
                                                        "correo": "lisa.williams@icloud.com",
                                                        "fotoPerfil": "lisawilliams.jpg",
                                                        "fechaRegistro": "2023-07-19:14-00-00"
                                                    },
                                                    {
                                                        "username": "jamesmoore",
                                                        "correo": "james.moore@yahoo.com",
                                                        "fotoPerfil": "jamesmoore.png",
                                                        "fechaRegistro": "2023-08-21:10-30-00"
                                                    },
                                                    {
                                                        "username": "patriciajones",
                                                        "correo": "patricia.jones@gmail.com",
                                                        "fotoPerfil": "patriciajones.jpg",
                                                        "fechaRegistro": "2023-09-29:09-45-00"
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
    @GetMapping("/get/users-after-dates")
    public ResponseEntity<PaginacionDto<UserDto>> buscarUsuariosDespuesDe(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal Admin admin) {

        Page<UserDto> users = adminService.buscarUserPorRegistroMayor(fechaInicio, admin.getId(),pageable, false)
                .map(usuario -> UserDto.of(usuario, getImageUrl(usuario.getFotoPerfil())));


        return ResponseEntity.ok(PaginacionDto.of(users));
    }

    @Operation(summary = "Elimina un admin por su Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Admin eliminado correctamente",
                    content = @Content),

    })
    @DeleteMapping("/delete/admin/{id}")
    public ResponseEntity<?> eliminarAdmin(@PathVariable UUID id){
        adminService.eliminarAdmin(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Elimina un manager por su Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Manager eliminado correctamente",
                    content = @Content),

    })
    @DeleteMapping("/delete/manager/{id}")
    public ResponseEntity<?> eliminarManager(@PathVariable UUID id, @AuthenticationPrincipal User user){
        managerService.eliminarManager(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    public String getImageUrl(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();
    }
}
