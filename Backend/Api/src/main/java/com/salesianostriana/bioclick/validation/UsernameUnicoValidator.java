package com.salesianostriana.bioclick.validation;

import com.salesianostriana.bioclick.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class UsernameUnicoValidator implements ConstraintValidator<UsernameUnico, String> {

    @Autowired
    private UserRepository repo;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.hasText(s) && !repo.existsByUsername(s);
    }
}
