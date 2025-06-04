package com.salesianostriana.bioclick.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.PropertyAccessorFactory;

public class ValoresCamposCoincidientesValidator implements ConstraintValidator<ValoresCamposCoincidientes, Object> {

    private String field;
    private String fieldMatch;

    @Override
    public void initialize(ValoresCamposCoincidientes constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        Object fieldValue = PropertyAccessorFactory
                .forBeanPropertyAccess(o).getPropertyValue(field);
        Object fieldValueMatch = PropertyAccessorFactory
                .forBeanPropertyAccess(o).getPropertyValue(fieldMatch);

        if (fieldValue != null)
            return fieldValue.equals(fieldValueMatch);
        else
            return fieldValueMatch == null;

    }
}
