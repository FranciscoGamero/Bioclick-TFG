package com.salesianostriana.bioclick.util;

public record SearchCriteria(
        String key,
        String operation,
        Object value
) {
}
