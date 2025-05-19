package com.example.library.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookCopyRepositoryUnitTest {

    @Mock
    private BookCopyRepository bookCopyRepository;

    @Test
    void GIVEN_bookId_WHEN_deleteByBookId_THEN_repository_delete_called() {
        // Given
        Long bookId = 1L;

        // When
        bookCopyRepository.deleteByBookId(bookId);

        // Then
        verify(bookCopyRepository).deleteByBookId(bookId);
    }
}