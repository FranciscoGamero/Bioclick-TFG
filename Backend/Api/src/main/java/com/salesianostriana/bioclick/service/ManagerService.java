package com.salesianostriana.bioclick.service;


import com.salesianostriana.bioclick.dto.manager.CreateManagerDto;
import com.salesianostriana.bioclick.dto.manager.EditManagerDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;
    private final SendGridMailSender mailSender;
    private final EntityManager entityManager;

    public Manager createManager(CreateManagerDto createManagerDto, UUID adminId, MultipartFile file) {

        FileMetadata fileMetadata = storageService.store(file);

        Admin adminCreador = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado al admin"));

        String metodoActual = Thread.currentThread().getStackTrace()[1].getMethodName();
        adminCreador.setUltimaAccion(metodoActual);
        adminCreador.setFechaUltimaAccion(LocalDateTime.now());
        adminRepository.save(adminCreador);


        Manager manager = Manager.builder()
                .username(createManagerDto.username())
                .password(passwordEncoder.encode(createManagerDto.password()))
                .correo(createManagerDto.correo())
                .fotoPerfil(fileMetadata.getFilename())
                .verificationCode(generateCode())
                .fechaRegistro(LocalDateTime.now())
                .ultimaAccionProducto("None")
                .fechaUltimaAccionProducto(LocalDateTime.now())
                .role("ROLE_MANAGER")
                .build();
        System.out.println(manager.getVerificationCode());

        try {
            String texto = "Su código para activar la cuenta es el siguiente: "+manager.getVerificationCode();
            mailSender.sendMail(createManagerDto.correo(), "Código de activación de cuenta", texto);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error al enviar el email de activación");
        }

        return managerRepository.save(manager);
    }

    public String generateCode(){
        return String.format("%06d", new Random().nextInt(99999));
    }

    public Manager editaManager(UUID adminId, UUID managerId, EditManagerDto editManagerDto, MultipartFile file) {

        FileMetadata fileMetadata;

        if (!file.isEmpty()) {
            fileMetadata = storageService.store(file);
        } else {
            fileMetadata = null;
        }

        Admin adminCreador = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado al admin"));

        String metodoActual = Thread.currentThread().getStackTrace()[1].getMethodName();
        adminCreador.setUltimaAccion(metodoActual);
        adminCreador.setFechaUltimaAccion(LocalDateTime.now());
        adminRepository.save(adminCreador);

        return managerRepository.findById(managerId).map(old -> {
            if(!Objects.equals(old.getUsername(), editManagerDto.username())){
                old.setUsername(editManagerDto.username());

            }
            if(!Objects.equals(old.getCorreo(), editManagerDto.correo())){
                old.setCorreo(editManagerDto.correo());

            }

            if (fileMetadata != null) {
                old.setFotoPerfil(fileMetadata.getFilename());
            }

            return managerRepository.save(old);
        }).orElseThrow(() -> new EntityNotFoundException("No se pudo editar al manager con id: " + editManagerDto));
    }


    public Manager managerEditarse(EditManagerDto editManagerDto, Manager manager, MultipartFile file) {

        FileMetadata fileMetadata = storageService.store(file);

        manager.setUsername(editManagerDto.username());
        manager.setCorreo(editManagerDto.correo());
        manager.setFotoPerfil(fileMetadata.getFilename());

        return managerRepository.save(manager);
    }

    public Page<Manager> buscarTodos(UUID id, Pageable pageable, boolean borrado) {

        User u = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado al usuario"));

        if (u instanceof Admin adminCreador) {
            String metodoActual = Thread.currentThread().getStackTrace()[1].getMethodName();
            adminCreador.setUltimaAccion(metodoActual);
            adminCreador.setFechaUltimaAccion(LocalDateTime.now());
            adminRepository.save(adminCreador);
        }

        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("userBorradoFiltro");
        filter.setParameter("isBorrado", borrado);

        try {
            return managerRepository.findAll(pageable);
        } finally {
            session.disableFilter("userBorradoFiltro");
        }
    }
    public void eliminarManager(UUID managerId, UUID adminId){

        User u = userRepository.findById(adminId).orElseThrow(() -> new EntityNotFoundException("No se ha encontrado al admin"));


        if(u.getClass().equals(Admin.class)){
            Admin adminCreador = adminRepository.findById(u.getId())
                    .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado al admin"));

            String metodoActual = Thread.currentThread().getStackTrace()[1].getMethodName();
            adminCreador.setUltimaAccion(metodoActual);
            adminCreador.setFechaUltimaAccion(LocalDateTime.now());
            adminRepository.save(adminCreador);
        }

        userRepository.deleteById(managerId);
    }
}
