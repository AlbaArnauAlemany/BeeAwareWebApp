package ch.unil.doplab.beeaware.ui;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

import java.util.regex.Pattern;

@FacesValidator("npaAndCountryValidator")
public class NPAAndCountryValidator implements Validator<String> {

    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        if (value == null || value.isEmpty()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password is required.", null));
        }

        int categoryCount = 0;
        final int LENGTH_VALID = 8;

        if (Pattern.compile("[A-Z]").matcher(value).find()) categoryCount++;
        if (Pattern.compile("[a-z]").matcher(value).find()) categoryCount++;
        if (Pattern.compile("[0-9]").matcher(value).find()) categoryCount++;
        if (Pattern.compile("[^a-zA-Z0-9]").matcher(value).find()) categoryCount++;

        if (categoryCount < 3 || value.length() < LENGTH_VALID) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Weak password: Minimal length 8 and please include at least 3 of the following categories: uppercase letters, lowercase letters, numbers, and special characters.",
                    null));
        }
    }
}

