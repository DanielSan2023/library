package com.example.library.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookRepositoryUnitTest {

    @Mock
    private BookRepository bookRepository;

    @Test
    void GIVEN_existingIsbn_WHEN_existsByIsbn_THEN_returnTrue() {
        // Given
        String isbn = "1234567890";
        when(bookRepository.existsByIsbn(isbn)).thenReturn(true);

        // When
        boolean exists = bookRepository.existsByIsbn(isbn);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void GIVEN_nonExistingIsbn_WHEN_existsByIsbn_THEN_returnFalse() {
        // Given
        String isbn = "no-exist-isbn";
        when(bookRepository.existsByIsbn(isbn)).thenReturn(false);

        // When
        boolean exists = bookRepository.existsByIsbn(isbn);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void GIVEN_existingTitle_WHEN_existsByTitle_THEN_returnTrue() {
        // Given
        String title = "Some Book Title";
        when(bookRepository.existsByTitle(title)).thenReturn(true);

        // When
        boolean exists = bookRepository.existsByTitle(title);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void GIVEN_nonExistingTitle_WHEN_existsByTitle_THEN_returnFalse() {
        // Given
        String title = "No Existing Title";
        when(bookRepository.existsByTitle(title)).thenReturn(false);

        // When
        boolean exists = bookRepository.existsByTitle(title);

        // Then
        assertThat(exists).isFalse();
    }
}