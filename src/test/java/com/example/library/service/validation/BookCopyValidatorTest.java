package com.example.library.service.validation;

import com.example.library.entity.BookCopy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.example.library.utility.BookConstants.NO_MATCHING_BOOK_COPIES;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BookCopyValidatorTest {

    private BookCopyValidator validator;

    @BeforeEach
    void setUp() {
        validator = new BookCopyValidator();
    }

    @Test
    void GIVEN_no_empty_list_WHEN_validateBookCopyAvailability_THEN_no_exception_thrown() {
        // Given
        BookCopy bookCopy = new BookCopy();
        List<BookCopy> copies = List.of(bookCopy);

        // When / Then
        assertThatCode(() -> validator.validateBookCopyAvailability(copies))
                .doesNotThrowAnyException();
    }

    @Test
    void GIVEN_empty_list_WHEN_validateBookCopyAvailability_THEN_throw_IllegalArgumentException() {
        // Given
        List<BookCopy> emptyList = Collections.emptyList();

        // When / Then
        assertThatThrownBy(() -> validator.validateBookCopyAvailability(emptyList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(NO_MATCHING_BOOK_COPIES);
    }
}
