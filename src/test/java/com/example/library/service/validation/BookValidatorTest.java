package com.example.library.service.validation;

import com.example.library.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class BookValidatorTest {

    private final BookValidator validator = new BookValidator();


    @Test
    void GIVEN_null_isbn_WHEN_validateIsbn_THEN_throw_IllegalArgumentException() {
        assertThatThrownBy(() -> validator.validateIsbn(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ISBN cannot be null");
    }

    @Test
    void GIVEN_invalid_isbn_WHEN_validateIsbn_THEN_throw_IllegalArgumentException() {
        assertThatThrownBy(() -> validator.validateIsbn("abc123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid ISBN format");
    }

    @Test
    void GIVEN_valid_isbn_WHEN_validateIsbn_THEN_pass_without_exception() {
        assertDoesNotThrow(() -> validator.validateIsbn("1234567890"));
        assertDoesNotThrow(() -> validator.validateIsbn("1234567890123"));
        assertDoesNotThrow(() -> validator.validateIsbn("123-456-7890"));
    }

    @Test
    void GIVEN_invalid_year_WHEN_validatePublishedYear_THEN_throw_IllegalArgumentException() {
        int tooEarly = 1000;
        int futureYear = 3000;

        assertThatThrownBy(() -> validator.validatePublishedYear(tooEarly))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid published year");

        assertThatThrownBy(() -> validator.validatePublishedYear(futureYear))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid published year");

        assertThatThrownBy(() -> validator.validatePublishedYear(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid published year");
    }

    @Test
    void GIVEN_valid_year_WHEN_validatePublishedYear_THEN_pass_without_exception() {
        int currentYear = java.time.Year.now().getValue();

        assertDoesNotThrow(() -> validator.validatePublishedYear(currentYear));
        assertDoesNotThrow(() -> validator.validatePublishedYear(1500));
    }

    @Test
    void GIVEN_mismatched_isbns_WHEN_validateIsbnConsistency_THEN_throw_ValidationException() {
        assertThatThrownBy(() -> validator.validateIsbnConsistency("1234", "5678"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Book ISBN mismatch");
    }

    @Test
    void GIVEN_matching_isbns_WHEN_validateIsbnConsistency_THEN_pass_without_exception() {
        assertDoesNotThrow(() -> validator.validateIsbnConsistency("1234567890", "1234567890"));
    }
}