package rockseat.com.passin.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import rockseat.com.passin.dto.event.EventRequestDTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return EventRequestDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EventRequestDTO event = (EventRequestDTO) target;
        String title = event.title();
        String details = event.details();
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9_ .!]");
        Matcher titleMatcher = pattern.matcher(title);
        Matcher detailsMatcher = pattern.matcher(details);
        if (titleMatcher.find()) {
            errors.rejectValue("title", "invalid.title", "Contains invalid characters in the string");
        }

        if (detailsMatcher.find()) {
            errors.rejectValue("details", "invalid.details", "Contains invalid characters in the string");
        }
    }
}
