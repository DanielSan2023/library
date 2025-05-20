package com.example.library.service;

import com.example.library.dto.bookcopydtos.BookCopyDtoFull;
import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import com.example.library.entity.Book;
import com.example.library.entity.BookCopy;
import com.example.library.repository.BookCopyRepository;
import com.example.library.service.serviceimpl.serviceimpl.BookCopyServiceImpl;
import com.example.library.service.validation.BookCopyFetcherValidator;
import com.example.library.service.validation.BookFetcherValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookCopyServiceUnitTest {

    @Mock
    private BookCopyRepository bookCopyRepository;

    @Mock
    private BookFetcherValidator bookFetcherValidator;

    @Mock
    private BookCopyFetcherValidator bookCopyFetcherValidator;

    private ModelMapper modelMapper;
    private BookCopyServiceImpl bookCopyService;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        bookCopyService = new BookCopyServiceImpl(bookCopyRepository, modelMapper, bookFetcherValidator, bookCopyFetcherValidator);
    }

    @Test
    void GIVEN_bookId_WHEN_addCopyToBook_THEN_return_mappedDto() {
        // Given
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);

        BookCopy bookCopy = BookCopy.builder().book(book).available(true).build();
        BookCopy savedCopy = BookCopy.builder().id(2L).book(book).available(true).build();

        when(bookFetcherValidator.getBookIfExistInDB(bookId)).thenReturn(book);
        when(bookCopyRepository.save(any(BookCopy.class))).thenReturn(savedCopy);

        // When
        BookCopyDtoSimple result = bookCopyService.addCopyToBook(bookId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getClass()).isEqualTo(BookCopyDtoSimple.class);
        verify(bookFetcherValidator).getBookIfExistInDB(bookId);
        verify(bookCopyRepository).save(any(BookCopy.class));
    }

    @Test
    void GIVEN_valid_Ids_WHEN_updateAvailability_THEN_update_and_returnDto() {
        // Given
        Long bookId = 1L;
        Long copyId = 10L;
        boolean newAvailability = false;

        Book book = new Book();
        book.setId(bookId);

        BookCopy bookCopy = BookCopy.builder().id(copyId).book(book).available(true).build();
        BookCopy updatedCopy = BookCopy.builder().id(copyId).book(book).available(false).build();

        when(bookCopyFetcherValidator.getBookCopyIfExistInDB(copyId)).thenReturn(bookCopy);
        doNothing().when(bookCopyFetcherValidator).controlOwnershipBookCopyAndBook(bookId, bookCopy);
        when(bookCopyRepository.save(bookCopy)).thenReturn(updatedCopy);

        // When
        BookCopyDtoFull result = bookCopyService.updateAvailability(bookId, copyId, newAvailability);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getClass()).isEqualTo(BookCopyDtoFull.class);
        verify(bookCopyFetcherValidator).getBookCopyIfExistInDB(copyId);
        verify(bookCopyFetcherValidator).controlOwnershipBookCopyAndBook(bookId, bookCopy);
        verify(bookCopyRepository).save(bookCopy);
    }


    @Test
    void GIVEN_no_exists_copy_WHEN_updateAvailability_THEN_throw_EntityNotFound() {
        // Given
        Long bookId = 1L;
        Long copyId = 99L;

        when(bookCopyFetcherValidator.getBookCopyIfExistInDB(copyId))
                .thenThrow(new EntityNotFoundException("Book copy not found with id: " + copyId));

        // When / Then
        assertThatThrownBy(() -> bookCopyService.updateAvailability(bookId, copyId, true))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Book copy not found with id: " + copyId);
    }

    @Test
    void GIVEN_wrong_BookId_WHEN_updateAvailability_THEN_throw_IllegalArgument() {
        // Given
        Long bookId = 1L;
        Long copyId = 10L;

        Book incorrectBook = new Book();
        incorrectBook.setId(2L);

        BookCopy copy = BookCopy.builder().id(copyId).book(incorrectBook).available(true).build();

        when(bookCopyFetcherValidator.getBookCopyIfExistInDB(copyId)).thenReturn(copy);
        doThrow(new IllegalArgumentException("Book copy does not belong to the specified book"))
                .when(bookCopyFetcherValidator).controlOwnershipBookCopyAndBook(bookId, copy);

        // When / Then
        assertThatThrownBy(() -> bookCopyService.updateAvailability(bookId, copyId, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Book copy does not belong to the specified book");

        verify(bookCopyRepository, never()).save(any());
    }
}

