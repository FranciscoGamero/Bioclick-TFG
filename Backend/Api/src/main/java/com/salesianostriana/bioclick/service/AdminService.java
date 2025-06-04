package com.salesianostriana.bioclick.service;

import com.salesianostriana.bioclick.dto.CambiarRolRequest;
import com.salesianostriana.bioclick.dto.VerificationCodeRequest.VerificationCodeRequest;
import com.salesianostriana.bioclick.dto.admin.CreateAdminRequest;
import com.salesianostriana.bioclick.dto.admin.EditAdminDto;
import com.salesianostriana.bioclick.dto.user.EditUserDto;
import com.salesianostriana.bioclick.model.Admin;
import com.salesianostriana.bioclick.model.FileMetadata;
import com.salesianostriana.bioclick.model.Manager;
import com.salesianostriana.bioclick.model.User;
import com.salesianostriana.bioclick.repository.AdminRepository;
import com.salesianostriana.bioclick.repository.ManagerRepository;
import com.salesianostriana.bioclick.repository.UserRepository;
import com.salesianostriana.bioclick.util.SendGridMailSender;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SendGridMailSender mailSender;
    private final StorageService storageService;
    private final ManagerRepository managerRepository;
    private final EntityManager entityManager;


    public Admin createAdmin(CreateAdminRequest createAdminRequest, UUID adminId, MultipartFile file) {

        FileMetadata fileMetadata = storageService.store(file);

        Admin adminCreador = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado al admin"));

        String metodoActual = Thread.currentThread().getStackTrace()[1].getMethodName();
        adminCreador.setUltimaAccion(metodoActual);
        adminCreador.setFechaUltimaAccion(LocalDateTime.now());

        Admin admin = Admin.builder()
                .username(createAdminRequest.username())
                .password(passwordEncoder.encode(createAdminRequest.password()))
                .correo(createAdminRequest.correo())
                .fotoPerfil(fileMetadata.getFilename())
                .verificationCode(generateCode())
                .fechaRegistro(LocalDateTime.now())
                .ultimaAccion("None")
                .fechaUltimaAccion(LocalDateTime.now())
                .role("ROLE_ADMIN")
                .build();
        System.out.println(admin.getVerificationCode());

        try {
            String texto = "Su código para activar la cuenta es el siguiente: "+admin.getVerificationCode();
            mailSender.sendMail(createAdminRequest.correo(), "Código de activación de cuenta", texto);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error al enviar el email de activación");
        }

        return adminRepository.save(admin);
    }

    public String generateCode(){
        return String.format("%06d", new Random().nextInt(99999));
    }


    public Admin editarAdmin(UUID adminId, EditAdminDto editAdminDto, MultipartFile file) {

        FileMetadata fileMetadata = storageService.store(file);
        Admin adminCreador = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado al admin"));

        String metodoActual = Thread.currentThread().getStackTrace()[1].getMethodName();
        adminCreador.setUltimaAccion(metodoActual);
        adminCreador.setFechaUltimaAccion(LocalDateTime.now());
        adminRepository.save(adminCreador);

        return adminRepository.findById(adminId).map(old -> {

            old.setUsername(editAdminDto.username());
            old.setCorreo(editAdminDto.correo());
            old.setFotoPerfil(fileMetadata.getFilename());
            System.out.println(old.getId());
            return adminRepository.save(old);
        }).orElseThrow(() -> new EntityNotFoundException("No se pudo editar al admin con id: " + editAdminDto));
    }




    public Page<User> buscarUsuarios(Pageable pageable, UUID adminId, boolean borrado) {

        Admin adminCreador = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado al admin"));

        String metodoActual = Thread.currentThread().getStackTrace()[1].getMethodName();
        adminCreador.setUltimaAccion(metodoActual);
        adminCreador.setFechaUltimaAccion(LocalDateTime.now());
        adminRepository.save(adminCreador);

        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("userBorradoFiltro");
        filter.setParameter("isBorrado", borrado);
        Page<User> paginaResultado = userRepository.findAll(pageable);
        session.disableFilter("userBorradoFiltro");
        if (!paginaResultado.isEmpty())
            return paginaResultado;
        else
            throw new EntityNotFoundException("No hay usuarios encontrados");
    }

    public User editarUsuario(EditUserDto editUserDto, UUID id, UUID adminId, MultipartFile file) {

        FileMetadata fileMetadata = storageService.store(file);

        Admin adminCreador = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado al admin"));

        String metodoActual = Thread.currentThread().getStackTrace()[1].getMethodName();
        adminCreador.setUltimaAccion(metodoActual);
        adminCreador.setFechaUltimaAccion(LocalDateTime.now());
        adminRepository.save(adminCreador);

        return userRepository.findById(id).map(old -> {

            old.setUsername(editUserDto.username());
            old.setCorreo(editUserDto.correo());
            old.setFotoPerfil(fileMetadata.getFilename());
            System.out.println(old.getId());

            return userRepository.save(old);
        }).orElseThrow(() -> new EntityNotFoundException("No se pudo editar dicho usuario" + id));
    }

    public User buscarPorId(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No se ha encontrado un usuario con id: "+id));
    }

    public Page<Manager> buscarManagers(Pageable pageable, UUID adminId) {

        Page<Manager> paginaResultado = adminRepository.buscarManagers(pageable);

        Admin adminCreador = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado al admin"));

        String metodoActual = Thread.currentThread().getStackTrace()[1].getMethodName();
        adminCreador.setUltimaAccion(metodoActual);
        adminCreador.setFechaUltimaAccion(LocalDateTime.now());
        adminRepository.save(adminCreador);


        if (!paginaResultado.isEmpty())
            return paginaResultado;
        else
            throw new EntityNotFoundException("No hay usuarios encontrados");
    }

    public Manager buscarManagerPorId(UUID id) {
        return managerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No se ha encontrado un usuario con id: "+id));
    }

    public Admin verifyAdmin(VerificationCodeRequest verificationCodeRequest){
        Admin admin = adminRepository.findFirstByVerificationCode(verificationCodeRequest.code())
                .orElseThrow(() -> new EntityNotFoundException("no se encuentra al admin"));

        if(verificationCodeRequest.code().equals(admin.getVerificationCode())){
            admin.setEnabled(true);
            adminRepository.save(admin);
        }
        return admin;

    }

    public Admin editarEsteAdmin(Admin admin, EditAdminDto editAdminDto, MultipartFile file) {
        FileMetadata fileMetadata = storageService.store(file);

        String metodoActual = Thread.currentThread().getStackTrace()[1].getMethodName();
        admin.setUltimaAccion(metodoActual);
        admin.setFechaUltimaAccion(LocalDateTime.now());

        admin.setUsername(editAdminDto.username());
        admin.setCorreo(editAdminDto.correo());
        admin.setFotoPerfil(fileMetadata.getFilename());
        admin.setPassword(passwordEncoder.encode(editAdminDto.password()));
        System.out.println(admin.getId());
        return adminRepository.save(admin);
    }

    public Page<Admin> buscarAdmins(Pageable pageable, UUID adminId) {

        Page<Admin> paginaResultado = adminRepository.buscarAdmins(pageable);

        Admin adminCreador = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado al admin"));

        String metodoActual = Thread.currentThread().getStackTrace()[1].getMethodName();
        adminCreador.setUltimaAccion(metodoActual);
        adminCreador.setFechaUltimaAccion(LocalDateTime.now());
        adminRepository.save(adminCreador);


        if (!paginaResultado.isEmpty())
            return paginaResultado;
        else
            throw new EntityNotFoundException("No hay usuarios encontrados");
    }

    public User cambiarRol(UUID userId, UUID adminId, CambiarRolRequest cambiarRolRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Admin adminCreador = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado al admin"));

        String metodoActual = Thread.currentThread().getStackTrace()[1].getMethodName();
        adminCreador.setUltimaAccion(metodoActual);
        adminCreador.setFechaUltimaAccion(LocalDateTime.now());
        adminRepository.save(adminCreador);


        if (!cambiarRolRequest.nuevoRol().equals("ROLE_USUARIO") && !cambiarRolRequest.nuevoRol().equals("ROLE_ADMIN")) {
            throw new IllegalArgumentException("Rol no válido");
        }
        user.setRole(cambiarRolRequest.nuevoRol());

        return userRepository.save(user);
    }

    public Page<User> buscarUserPorNombreUsuario(String nombreUsuario, UUID adminId, Pageable pageable, boolean borrado) {

        Admin adminCreador = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado al admin"));

        String metodoActual = Thread.currentThread().getStackTrace()[1].getMethodName();
        adminCreador.setUltimaAccion(metodoActual);
        adminCreador.setFechaUltimaAccion(LocalDateTime.now());
        adminRepository.save(adminCreador);

        Specification<User> spec = Specification
                .where(User.nombreContiene(nombreUsuario));

        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("userBorradoFiltro");
        filter.setParameter("isBorrado", borrado);
        Page<User> listaUsuarios = userRepository.findAll(spec, pageable);
        session.disableFilter("userBorradoFiltro");
        return listaUsuarios;
    }

    public Page<User> buscarUserPorCorreo(String correo, UUID adminId, Pageable pageable, boolean borrado) {

        Admin adminCreador = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado al admin"));

        String metodoActual = Thread.currentThread().getStackTrace()[1].getMethodName();
        adminCreador.setUltimaAccion(metodoActual);
        adminCreador.setFechaUltimaAccion(LocalDateTime.now());
        adminRepository.save(adminCreador);

        Specification<User> spec = Specification
                .where(User.correoContiene(correo));

        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("userBorradoFiltro");
        filter.setParameter("isBorrado", borrado);
        Page<User> listaUsuarios = userRepository.findAll(spec, pageable);
        session.disableFilter("userBorradoFiltro");
        return listaUsuarios;
    }

    public Page<User> buscarUserPorRegistroMayor(LocalDate registro, UUID adminId, Pageable pageable, boolean borrado) {

        Admin adminCreador = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado al admin"));

        String metodoActual = Thread.currentThread().getStackTrace()[1].getMethodName();
        adminCreador.setUltimaAccion(metodoActual);
        adminCreador.setFechaUltimaAccion(LocalDateTime.now());
        adminRepository.save(adminCreador);

        if(registro == null)
            registro = LocalDate.now();

        Specification<User> spec = Specification
                .where(User.registroMayorQue(LocalDateTime.of(registro, LocalTime.of(0,0,0))));

        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("userBorradoFiltro");
        filter.setParameter("isBorrado", borrado);
        Page<User> listaUsuarios = userRepository.findAll(spec, pageable);
        session.disableFilter("userBorradoFiltro");
        return listaUsuarios;
    }

    public Page<User> buscarUserPorRegistroMenor(LocalDate registro, UUID adminId, Pageable pageable, boolean borrado) {

        Admin adminCreador = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado al admin"));

        String metodoActual = Thread.currentThread().getStackTrace()[1].getMethodName();
        adminCreador.setUltimaAccion(metodoActual);
        adminCreador.setFechaUltimaAccion(LocalDateTime.now());
        adminRepository.save(adminCreador);

        if(registro == null)
            registro = LocalDate.now();

        Specification<User> spec = Specification
                .where(User.registroMenorQue(LocalDateTime.of(registro, LocalTime.of(0,0,0))));

        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("userBorradoFiltro");
        filter.setParameter("isBorrado", borrado);
        Page<User> listaUsuarios = userRepository.findAll(spec, pageable);
        session.disableFilter("userBorradoFiltro");
        return listaUsuarios;
    }

    public Page<User> buscarUserEntreFechas(LocalDate fechaInicio, UUID adminId, LocalDate fechaFinal, Pageable pageable, boolean borrado) {

        Admin adminCreador = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado al admin"));

        String metodoActual = Thread.currentThread().getStackTrace()[1].getMethodName();
        adminCreador.setUltimaAccion(metodoActual);
        adminCreador.setFechaUltimaAccion(LocalDateTime.now());
        adminRepository.save(adminCreador);

        if(fechaInicio == null){
            fechaInicio = LocalDate.now().minusDays(1L);
        }
        if (fechaFinal == null){
            fechaFinal =  LocalDate.now().minusDays(1L);
        }

        Specification<User> spec = Specification
                .where(User.registroMayorQue(LocalDateTime.of(fechaInicio, LocalTime.of(0, 0, 0)))
                        .and(User.registroMenorQue(LocalDateTime.of(fechaFinal, LocalTime.of(23, 59, 59)))));


        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("userBorradoFiltro");
        filter.setParameter("isBorrado", borrado);
        Page<User> listaUsuarios = userRepository.findAll(spec, pageable);
        session.disableFilter("userBorradoFiltro");
        return listaUsuarios;
    }
    public void eliminarAdmin(UUID adminId){
        userRepository.deleteById(adminId);
    }

}