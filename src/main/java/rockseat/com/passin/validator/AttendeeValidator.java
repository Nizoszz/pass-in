package rockseat.com.passin.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import rockseat.com.passin.dto.attendee.AttendeeRequestDTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttendeeValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return AttendeeRequestDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AttendeeRequestDTO event = (AttendeeRequestDTO) target;
        String name = event.name();
        String email = event.email();
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9_ .!]");
        Matcher nameMatcher = pattern.matcher(name);
        Matcher emailMatcher = pattern.matcher(email);
        if (nameMatcher.find()) {
            errors.rejectValue("name", "invalid.name", "Contains invalid characters in the string");
        }

        if (emailMatcher.find()) {
            errors.rejectValue("email", "invalid.email", "Contains invalid characters in the string");
        }
    }
}
