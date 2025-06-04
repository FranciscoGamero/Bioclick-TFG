package com.salesianostriana.bioclick.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValoresCamposCoincidientesValidator.class)
public @interface ValoresCamposCoincidientes {

    String message() default "Los valores de los campos no coinciden";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String field();
    String fieldMatch();

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        ValoresCamposCoincidientes[] value();
    }

}
