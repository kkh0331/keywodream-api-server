package pda.keywordream.utils.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class DateValidator implements ConstraintValidator<ValidDate, String> {

    private final Pattern DATE_PATTERN = Pattern.compile("\\d{8}");

    @Override
    public void initialize(ValidDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        return date != null && DATE_PATTERN.matcher(date).matches();
    }
}
