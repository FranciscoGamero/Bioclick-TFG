package com.salesianostriana.bioclick.service;

import com.salesianostriana.bioclick.dto.ImpactoAmbiental.CreateImpactoDto;
import com.salesianostriana.bioclick.model.ImpactoAmbiental;
import com.salesianostriana.bioclick.model.Producto;
import com.salesianostriana.bioclick.repository.ImpactoAmbientalRepository;
import com.salesianostriana.bioclick.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImpactoAmbientalService {

    private final ImpactoAmbientalRepository impactoAmbientalRepository;
    private final ProductoRepository productoRepository;

    public ImpactoAmbiental createImpactoAmbiental(CreateImpactoDto createImpactoDto) {
        Producto p = productoRepository
                .findById(UUID.fromString(createImpactoDto.productoId()))
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        ImpactoAmbiental impacto =  ImpactoAmbiental.builder()
                .ahorroMateriales(createImpactoDto.ahorroMateriales())
                .reduccionCo2(createImpactoDto.reduccionCo2())
                .producto(p)
                .build();

        p.setImpactoAmbiental(impacto);

        impactoAmbientalRepository.save(impacto);
        productoRepository.save(p);

        return impacto;
    }

    public Page<ImpactoAmbiental> buscarImpactos(Pageable pageable) {
        return impactoAmbientalRepository.buscarTodos(pageable);
    }

    public ImpactoAmbiental buscarUnImpacto(UUID impactoID) {
        return impactoAmbientalRepository.buscarUnImpacto(impactoID);
    }

    public ImpactoAmbiental editarImpacto(CreateImpactoDto editImpactDto, UUID impactId) {

        return impactoAmbientalRepository.findById(impactId).map(old -> {
            old.setAhorroMateriales(editImpactDto.ahorroMateriales());
            old.setReduccionCo2(editImpactDto.reduccionCo2());

            return impactoAmbientalRepository.save(old);
        }).orElseThrow(() -> new EntityNotFoundException("No se pudo editar dicho producto" + impactId));
    }
}
