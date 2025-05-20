package com.example.library.service.validation;

import com.example.library.exception.ValidationException;
import com.example.library.utility.BookConstants;
import org.springframework.stereotype.Component;

import java.time.Year;

import static com.example.library.utility.BookConstants.*;

@Component
public class BookValidator {


    public void validateIsbn(String isbn) {                                      //TODO add more robust validation
        if (isbn == null) {
            throw new IllegalArgumentException(ISBN_CANNOT_BE_NULL);
        }

        String cleanedIsbn = isbn.replace("-", "");
        String regex = String.format("\\d{%d}|\\d{%d}", BookConstants.MIN_TITLE_LENGTH, BookConstants.MAX_TITLE_LENGTH);

        if (!cleanedIsbn.matches(regex)) {
            throw new IllegalArgumentException(INVALID_ISBN_FORMAT);
        }
    }

    public void validatePublishedYear(Integer year) {                            //TODO add more robust validation
        int currentYear = Year.now().getValue();
        if (year == null || year < BookConstants.MIN_PUBLISHED_YEAR_BOOK || year > currentYear) {
            throw new IllegalArgumentException(INVALID_PUBLISHED_YEAR);
        }
    }

    public void validateIsbnConsistency(String existingIsbn, String requestIsbn) {
        if (!existingIsbn.equals(requestIsbn)) {
            throw new ValidationException(String.format(
                    ISBN_MISMATCH_EXCEPTION,
                    existingIsbn, requestIsbn
            ));
        }
    }
}
