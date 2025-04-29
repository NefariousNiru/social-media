package com.nefarious.socialnetwork.user.validator;

import com.nefarious.socialnetwork.user.aop.MinimumAge;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class MinimumAgeValidator implements ConstraintValidator<MinimumAge, LocalDate> {
    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext context) {
        if (dateOfBirth == null) return false;
        int minAge = 13;
        LocalDate minDate = LocalDate.now().minusYears(minAge);
        return dateOfBirth.isBefore(minDate) || dateOfBirth.isEqual(minDate);
    }
}
