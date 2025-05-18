package com.example.library.service.validation;

import com.example.library.entity.Book;
import com.example.library.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookFetcherValidatorTest {
    private final BookRepository bookRepository = mock(BookRepository.class);
    private final BookFetcherValidator validator = new BookFetcherValidator(bookRepository);

    @Test
    void GIVEN_exists_book_WHEN_getBookIfExistInDB_THEN_return_book() {
        //GIVEN
        Long bookId = 1L;
        Book book = new Book();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        //WHEN
        Book result = validator.getBookIfExistInDB(bookId);

        //THEN
        assertEquals(book, result);
        verify(bookRepository).findById(bookId);
    }

    @Test
    void GIVEN_non_exist_book_WHEN_getBookIfExistInDB_THEN_throwsException() {
        //GIVEN
        Long bookId = 2L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        //WHEN
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            validator.getBookIfExistInDB(bookId);
        });

        //THEN
        assertTrue(thrown.getMessage().contains("Book not found with id: " + bookId));
        verify(bookRepository).findById(bookId);
    }
}