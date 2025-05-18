package com.example.library.controller;

import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import com.example.library.dto.bookdtos.BookDtoFull;
import com.example.library.dto.bookdtos.BookDtoResponse;
import com.example.library.dto.bookdtos.BookDtoSimple;
import com.example.library.dto.bookdtos.BookDtoUpdate;
import com.example.library.exception.BookNotFoundException;
import com.example.library.service.serviceimpl.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class BookControllerUnitTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    void GIVEN_empty_book_list_WHEN_getAllBooks_THEN_return_Ok_Status_empty_list() {
        //GIVEN
        List<BookDtoSimple> books = List.of();
        when(bookService.getAllBooks()).thenReturn(books);

        //WHEN
        ResponseEntity<List<BookDtoSimple>> response = bookController.getAllBooks();

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void GIVEN_books_exists_WHEN_getAllBooks_THEN_return_Ok_Status_list_books() {
        //GIVEN
        List<BookDtoSimple> books = List.of(new BookDtoSimple(), new BookDtoSimple());
        when(bookService.getAllBooks()).thenReturn(books);

        //WHEN
        ResponseEntity<List<BookDtoSimple>> response = bookController.getAllBooks();

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(books);
    }

    @Test
    void GIVEN_valid_book_WHEN_createBook_THEN_return_Ok_Status_created_book() {
        //GIVEN
        BookDtoResponse book = new BookDtoResponse();
        when(bookService.createBook(book)).thenReturn(book);

        //WHEN
        ResponseEntity<BookDtoResponse> response = bookController.createBook(book);

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(book);
    }

    @Test
    void GIVEN_book_exists_WHEN_getBookById_THEN_return_Ok_Status_created_book() {
        //GIVEN
        long bookId = 1L;
        BookDtoFull book = new BookDtoFull();
        when(bookService.getBookById(bookId)).thenReturn(book);

        //WHEN
        ResponseEntity<BookDtoFull> response = bookController.getBookById(bookId);

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(book);
    }

    @Test
    void GIVEN_no_exists_bookId_WHEN_getBookById_THEN_throwBookNotFoundException() {
        // GIVEN
        long bookId = 10L;
        when(bookService.getBookById(bookId)).thenThrow(new BookNotFoundException("Book not found"));

        // WHEN / THEN
        BookNotFoundException ex = assertThrows(BookNotFoundException.class, () -> {
            bookController.getBookById(bookId);
        });

        assertThat(ex.getMessage()).isEqualTo("Book not found");
    }

    @Test
    void GIVEN_valid_updated_book_WHEN_updateBook_THEN_return_Ok_Status_updated_book() {
        //GIVEN
        long bookId = 1L;
        BookDtoUpdate update = new BookDtoUpdate();
        BookDtoFull updatedBook = new BookDtoFull();
        when(bookService.updateBook(bookId, update)).thenReturn(updatedBook);

        //WHEN
        ResponseEntity<BookDtoFull> response = bookController.updateBook(bookId, update);

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedBook);
    }

    @Test
    void GIVEN_exists_bookId_WHEN_deleteBook_THEN_return_NO_CONTENT_Status() {
        //GIVEN
        long bookId = 1L;

        //WHEN
        ResponseEntity<Void> response = bookController.deleteBook(bookId);

        //THEN
        verify(bookService).deleteBook(bookId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void GIVEN_no_exists_BookId_WHEN_deleteBook_THEN_throw_BookNotFoundException() {
        // GIVEN
        long bookId = 1L;
        doThrow(new BookNotFoundException("Book not found")).when(bookService).deleteBook(bookId);

        // WHEN / THEN
        BookNotFoundException ex = assertThrows(BookNotFoundException.class, () -> {
            bookController.deleteBook(bookId);
        });

        assertThat(ex.getMessage()).isEqualTo("Book not found");
    }

    @Test
    void GIVEN_copies_exists_WHEN_getAllBookCopies_THEN_return_Ok_Status_copies_list() {
        //GIVEN
        long bookId = 1L;
        List<BookCopyDtoSimple> copies = List.of(new BookCopyDtoSimple(), new BookCopyDtoSimple());
        when(bookService.getCopiesByBookId(bookId)).thenReturn(copies);

        //WHEN
        ResponseEntity<List<BookCopyDtoSimple>> response = bookController.getAllBookCopies(bookId);

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(copies);
    }

    @Test
    void GIVEN_copies_no_exists_WHEN_getAllBookCopies_THEN_return_Ok_Status_empty_copies_list() {
        //GIVEN
        long bookId = 1L;
        List<BookCopyDtoSimple> copies = List.of();
        when(bookService.getCopiesByBookId(bookId)).thenReturn(copies);

        //WHEN
        ResponseEntity<List<BookCopyDtoSimple>> response = bookController.getAllBookCopies(bookId);

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void GIVEN_no_exists_bookId_WHEN_getAllBookCopies_THEN_throwBookNotFoundException() {
        // GIVEN
        long bookId = 99L;
        when(bookService.getCopiesByBookId(bookId)).thenThrow(new BookNotFoundException("Book not found"));

        // WHEN / THEN
        BookNotFoundException ex = assertThrows(BookNotFoundException.class, () -> {
            bookController.getAllBookCopies(bookId);
        });

        assertThat(ex.getMessage()).isEqualTo("Book not found");
    }
}



