package com.salesianostriana.bioclick.service;

import com.salesianostriana.bioclick.dto.producto.CreateProductoDto;
import com.salesianostriana.bioclick.model.*;
import com.salesianostriana.bioclick.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final EntityManager entityManager;
    private final StorageService storageService;
    private final ImpactoAmbientalRepository impactoAmbientalRepository;



    @Transactional
    public Producto createProducto(CreateProductoDto createProductoDto, UUID id, MultipartFile file) {

        FileMetadata fileMetadata = storageService.store(file);


        User creador = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Creador no encontrado"));

        Producto producto = Producto.builder()
                .nombreProducto(createProductoDto.nombreProducto())
                .descripcion(createProductoDto.descripcion())
                .precioProducto(createProductoDto.precioProducto())
                .imagenProducto(fileMetadata.getFilename())
                .estado(createProductoDto.estado())
                .build();

        if (creador.getClass().equals(Admin.class)) {
            Admin adminCreador = (Admin) creador;
            producto.addCreador(adminCreador);
            adminRepository.save(adminCreador);
        } else if (creador.getClass().equals(Manager.class)) {
            Manager managerCreador = (Manager) creador;
            producto.addCreador(managerCreador);
            managerRepository.save(managerCreador);
        }

        productoRepository.save(producto);

        return producto;
    }


    public Page<Producto> buscarProductos(Pageable pageable, boolean borrado) {

        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("productoBorradoFiltro");
        filter.setParameter("isBorrado", borrado);
        Page<Producto> paginaResultado = productoRepository.buscarProductos(pageable);
        session.disableFilter("productoBorradoFiltro");

        if (!paginaResultado.isEmpty())
            return paginaResultado;
        else
            throw new EntityNotFoundException("No hay productos encontrados");
    }

    public Producto buscarPorId(UUID productId) {

        return productoRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("No se ha encontrado un producto con id: "+productId));

    }


    public Producto editarProducto(CreateProductoDto editProductDto, UUID productId, MultipartFile file, UUID creadorId) {

        String metodoActual = Thread.currentThread().getStackTrace()[1].getMethodName();
        User creador = userRepository.findById(creadorId).orElseThrow(() -> new EntityNotFoundException("Creador no encontrado"));

        if (creador.getClass().equals(Admin.class)) {

            Admin adminCreador = (Admin) creador;

            adminCreador.setUltimaAccion(metodoActual);
            adminCreador.setFechaUltimaAccion(LocalDateTime.now());
            adminRepository.save(adminCreador);

        } else if (creador.getClass().equals(Manager.class)) {

            Manager managerCreador = (Manager) creador;

            managerCreador.setUltimaAccionProducto(metodoActual);
            managerCreador.setFechaUltimaAccionProducto(LocalDateTime.now());
            managerRepository.save(managerCreador);
        }


        FileMetadata fileMetadata = storageService.store(file);


        return productoRepository.findById(productId).map(old -> {

            old.setNombreProducto(editProductDto.nombreProducto());
            old.setDescripcion(editProductDto.descripcion());
            old.setPrecioProducto(editProductDto.precioProducto());
            old.setImagenProducto(fileMetadata.getFilename());
            old.setEstado(editProductDto.estado());
            return productoRepository.save(old);
        }).orElseThrow(() -> new EntityNotFoundException("No se pudo editar dicho producto" + productId));
    }

    @Transactional
    public void eliminarProductoPorId(UUID id) {
        productoRepository.deleteById(id);
        productoRepository.borrarValoracionesProducto(id);
        productoRepository.borrarFavoritosProducto(id);
        productoRepository.borrarImpactoAmbientalProducto(id);
    }
}
