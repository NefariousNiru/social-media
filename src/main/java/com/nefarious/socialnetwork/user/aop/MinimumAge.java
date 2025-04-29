package com.nefarious.socialnetwork.user.aop;

import com.nefarious.socialnetwork.user.validator.MinimumAgeValidator;
import com.nefarious.socialnetwork.user.util.Constants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/** Enforces user be of Minimum age for registration */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinimumAgeValidator.class)
@Documented
public @interface MinimumAge {
    String message() default Constants.INVALID_AGE;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
