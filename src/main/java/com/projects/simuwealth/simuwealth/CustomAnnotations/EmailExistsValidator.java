package com.projects.simuwealth.simuwealth.CustomAnnotations;

import com.projects.simuwealth.simuwealth.Repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailExistsValidator implements ConstraintValidator<EmailExists, String> {

    @Autowired
    UserRepository userRepository;

    @Override
    public void initialize(EmailExists constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return userRepository.findByEmail(email) != null;
    }
}
