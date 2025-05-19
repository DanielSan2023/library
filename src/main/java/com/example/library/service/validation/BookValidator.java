package com.example.library.service.validation;

import com.example.library.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.time.Year;

@Component
public class BookValidator {
    public static final int MIN_TITLE_LENGTH = 10;
    public static final int MAX_TITLE_LENGTH = 13;

    public static final int MIN_PUBLISHED_YEAR_BOOK = 1443;

    public void validateIsbn(String isbn) {                                      //TODO add more robust validation
        if (isbn == null) {
            throw new IllegalArgumentException("ISBN cannot be null");
        }

        String cleanedIsbn = isbn.replace("-", "");
        String regex = String.format("\\d{%d}|\\d{%d}", MIN_TITLE_LENGTH, MAX_TITLE_LENGTH);

        if (!cleanedIsbn.matches(regex)) {
            throw new IllegalArgumentException("Invalid ISBN format");
        }
    }

    public void validatePublishedYear(Integer year) {                            //TODO add more robust validation
        int currentYear = Year.now().getValue();
        if (year == null || year < MIN_PUBLISHED_YEAR_BOOK || year > currentYear) {
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
