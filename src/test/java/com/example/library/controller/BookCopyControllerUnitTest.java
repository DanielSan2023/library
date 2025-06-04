package com.example.library.controller;

import com.example.library.dto.bookcopydtos.BookCopyDtoFull;
import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import com.example.library.dto.bookcopydtos.BookCopyDtoUpdate;
import com.example.library.service.service.BookCopyService;
import com.example.library.exception.BookNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookCopyControllerUnitTest {

    @Mock
    private BookCopyService bookCopyService;

    @InjectMocks
    private BookCopyController bookCopyController;

    @Test
    void GIVEN_valid_bookId_WHEN_addCopyToBook_THEN_return_Created_Status_copy_list() {
        // GIVEN
        Long bookId = 1L;
        BookCopyDtoSimple createdCopy = new BookCopyDtoSimple();
        when(bookCopyService.addCopyToBook(bookId)).thenReturn(createdCopy);

        // WHEN
        ResponseEntity<BookCopyDtoSimple> response = bookCopyController.addCopyToBook(bookId);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(createdCopy);
        verify(bookCopyService).addCopyToBook(bookId);
    }

    @Test
    void GIVEN_invalid_bookId_WHEN_addCopyToBook_THEN_throwBookNotFoundException() {
        // GIVEN
        Long invalidBookId = 999L;
        when(bookCopyService.addCopyToBook(invalidBookId)).thenThrow(new BookNotFoundException("Book not found"));

        // WHEN / THEN
        assertThatThrownBy(() -> bookCopyController.addCopyToBook(invalidBookId))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("Book not found");

        verify(bookCopyService).addCopyToBook(invalidBookId);
    }

    @Test
    void GIVEN_valid_updateRequest_WHEN_updateBookCopyAvailability_THEN_return_Ok_Status_updated_copy() {
        // GIVEN
        Long bookId = 1L;
        Long copyId = 10L;
        BookCopyDtoUpdate updateDto = new BookCopyDtoUpdate();
        updateDto.setAvailable(true);
        BookCopyDtoFull updatedCopy = new BookCopyDtoFull();

        when(bookCopyService.updateAvailability(bookId, copyId, updateDto.getAvailable())).thenReturn(updatedCopy);

        // WHEN
        ResponseEntity<BookCopyDtoFull> response = bookCopyController.updateBookCopyAvailability(bookId, copyId, updateDto);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedCopy);
        verify(bookCopyService).updateAvailability(bookId, copyId, updateDto.getAvailable());
    }

    @Test
    void GIVEN_invalid_bookId_WHEN_updateBookCopyAvailability_THEN_throwBookNotFoundException() {
        // GIVEN
        Long invalidBookId = 999L;
        Long copyId = 1L;
        BookCopyDtoUpdate updateDto = new BookCopyDtoUpdate();
        updateDto.setAvailable(true);

        when(bookCopyService.updateAvailability(invalidBookId, copyId, updateDto.getAvailable()))
                .thenThrow(new BookNotFoundException("Book not found"));

        // WHEN / THEN
        assertThatThrownBy(() -> bookCopyController.updateBookCopyAvailability(invalidBookId, copyId, updateDto))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("Book not found");

        verify(bookCopyService).updateAvailability(invalidBookId, copyId, updateDto.getAvailable());
    }

    @Test
    void GIVEN_invalid_copyId_WHEN_updateBookCopyAvailability_THEN_throwBookNotFoundException() {
        // GIVEN
        Long bookId = 1L;
        Long invalidCopyId = 999L;
        BookCopyDtoUpdate updateDto = new BookCopyDtoUpdate();
        updateDto.setAvailable(true);

        when(bookCopyService.updateAvailability(bookId, invalidCopyId, updateDto.getAvailable()))
                .thenThrow(new BookNotFoundException("Copy not found"));

        // WHEN / THEN
        assertThatThrownBy(() -> bookCopyController.updateBookCopyAvailability(bookId, invalidCopyId, updateDto))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("Copy not found");

        verify(bookCopyService).updateAvailability(bookId, invalidCopyId, updateDto.getAvailable());
    }
}
