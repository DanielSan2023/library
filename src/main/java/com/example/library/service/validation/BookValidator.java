package com.example.library.service.validation;

import com.example.library.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.time.Year;

@Component
public class BookValidator {

    public void validateIsbn(String isbn) {                                      //TODO add more robust validation
        if (isbn == null || !isbn.replace("-", "").matches("\\d{10}|\\d{13}")) {
            throw new IllegalArgumentException("Invalid ISBN format");
        }
    }

    public void validatePublishedYear(Integer year) {                            //TODO add more robust validation
        int currentYear = Year.now().getValue();
        if (year == null || year < 1443 || year > currentYear) {
            throw new IllegalArgumentException("Invalid published year");
        }
    }

    public void validateIsbnConsistency(String existingIsbn, String requestIsbn) {
        if (!existingIsbn.equals(requestIsbn)) {
            throw new ValidationException(String.format(
                    "Book ISBN mismatch: existing = '%s', requested = '%s'",
                    existingIsbn, requestIsbn
            ));
        }
    }
}
