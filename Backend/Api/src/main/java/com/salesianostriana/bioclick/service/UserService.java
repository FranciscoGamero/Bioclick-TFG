package com.salesianostriana.bioclick.service;

import com.salesianostriana.bioclick.dto.user.CreateUserRequest;
import com.salesianostriana.bioclick.dto.VerificationCodeRequest.VerificationCodeRequest;
import com.salesianostriana.bioclick.dto.user.EditUserDto;
import com.salesianostriana.bioclick.model.*;
import com.salesianostriana.bioclick.repository.CategoriaRepository;
import com.salesianostriana.bioclick.repository.ProductoRepository;
import com.salesianostriana.bioclick.repository.UserRepository;
import com.salesianostriana.bioclick.util.SendGridMailSender;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

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


import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Log
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProductoRepository productoRepository;
    private final PasswordEncoder passwordEncoder;
    private final SendGridMailSender mailSender;
    private final EntityManager entityManager;
    private final StorageService storageService;

    public User createUser(CreateUserRequest createUserRequest, MultipartFile file) {

        FileMetadata fileMetadata = storageService.store(file);


        User user = User.builder()
                .username(createUserRequest.username())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .correo(createUserRequest.correo())
                .fotoPerfil(fileMetadata.getFilename())
                .verificationCode(generateCode())
                .fechaRegistro(LocalDateTime.now())
                .enabled(false)
                .build();
        System.out.println(user.getVerificationCode() +" "+ user.getAuthorities());

        userRepository.save(user);

        try {
            String texto = "Su código para activar la cuenta es el siguiente: "+user.getVerificationCode();
            mailSender.sendMail(createUserRequest.correo(), "Código de activación de cuenta", texto);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error al enviar el email de activación");
        }

        return user;
    }
    public String generateCode(){
        return String.format("%06d", new Random().nextInt(99999));
    }

    public User verifyUser(VerificationCodeRequest user){
        User user1 = userRepository.findFirstByVerificationCode(user.code())
                .orElseThrow(() -> new EntityNotFoundException("no se encuentra al usuario"));

        if(user.code().equals(user1.getVerificationCode())){
            user1.setEnabled(true);
            userRepository.save(user1);
        }
        return user1;

    }


    public User editarUsuario(User user, EditUserDto editUserDto, MultipartFile file) {

        FileMetadata fileMetadata = storageService.store(file);

        user.setUsername(editUserDto.username());
        user.setCorreo(editUserDto.correo());
        user.setFotoPerfil(fileMetadata.getFilename());
        user.setPassword(passwordEncoder.encode(editUserDto.password()));
        return userRepository.save(user);
    }
    public Page<Producto> buscarProductoPorPrecioEntreMedio(Double min, Double max, Pageable pageable, boolean borrado) {

        if(min == null){
            min = 0.0;
        }
        if (max == null){
            max = 99999.9;
        }

        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("productoBorradoFiltro");
        filter.setParameter("isBorrado", borrado);

        Specification<Producto> spec = Specification
                .where(Producto.precioMayorQue(min))
                .and(Producto.precioMenorQue(max));

        Page<Producto> productos = productoRepository.findAll(spec, pageable);
        session.disableFilter("productoBorradoFiltro");
        return productos;
    }

    public Page<Producto> buscarProductoPorPrecioMayor(Double min, Pageable pageable, boolean borrado) {

        if(min == null){
            min = 0.0;
        }

        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("productoBorradoFiltro");
        filter.setParameter("isBorrado", borrado);

        Specification<Producto> spec = Specification
                .where(Producto.precioMayorQue(min));

        Page<Producto> productos = productoRepository.findAll(spec, pageable);
        session.disableFilter("productoBorradoFiltro");
        return productos;
    }

    public Page<Producto> buscarProductoPorPrecioMenor(Double max, Pageable pageable, boolean borrado) {

        if(max == null){
            max = 99999.9;
        }


        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("productoBorradoFiltro");
        filter.setParameter("isBorrado", borrado);

        Specification<Producto> spec = Specification
                .where(Producto.precioMenorQue(max));

        Page<Producto> productos = productoRepository.findAll(spec, pageable);
        session.disableFilter("productoBorradoFiltro");
        return productos;
    }

    public Page<Producto> buscarProductoPorNombre(String nombre, Pageable pageable, Boolean borrado) {

        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("productoBorradoFiltro");
        filter.setParameter("isBorrado", borrado);

        Specification<Producto> spec = Specification
                .where(Producto.nombreContiene(nombre));

        Page<Producto> productos = productoRepository.findAll(spec, pageable);
        session.disableFilter("productoBorradoFiltro");
        return productos;

    }


    public void eliminarUsuarioPorId(UUID id) {
        userRepository.deleteById(id);
    }

}
