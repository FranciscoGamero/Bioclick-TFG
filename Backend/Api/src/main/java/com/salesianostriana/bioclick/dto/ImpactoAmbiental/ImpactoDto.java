package com.salesianostriana.bioclick.dto.ImpactoAmbiental;

import com.salesianostriana.bioclick.model.ImpactoAmbiental;

import java.util.Set;

public record ImpactoDto(
        String nombreProducto,
        Double reduccionCo2,
        Set<String> ahorroMateriales
) {
    public static ImpactoDto of(ImpactoAmbiental impactoAmbiental) {
        return new ImpactoDto(
                impactoAmbiental.getProducto().getNombreProducto(),
                impactoAmbiental.getReduccionCo2(),
                impactoAmbiental.getAhorroMateriales());
    }
}
