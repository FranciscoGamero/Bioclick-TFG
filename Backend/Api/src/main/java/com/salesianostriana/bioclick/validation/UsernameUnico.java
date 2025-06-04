package com.salesianostriana.bioclick.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameUnicoValidator.class)
@Documented
public @interface UsernameUnico {

    String message() default "El nombre de usuario no puede estar repetido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
