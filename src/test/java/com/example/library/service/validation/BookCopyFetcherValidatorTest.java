package com.example.library.service.validation;

import com.example.library.entity.Book;
import com.example.library.entity.BookCopy;
import com.example.library.repository.BookCopyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.library.utility.BookConstants.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookCopyFetcherValidatorTest {

    private final BookCopyRepository bookCopyRepository = mock(BookCopyRepository.class);
    private final BookCopyFetcherValidator validator = new BookCopyFetcherValidator(bookCopyRepository);

    @Test
    void GIVEN_exists_copy_id_WHEN_getBookCopyIfExistInDB_THEN_return_copy() {
        //GIVEN
        BookCopy copy = new BookCopy();

        //WHEN
        when(bookCopyRepository.findById(1L)).thenReturn(Optional.of(copy));

        assertDoesNotThrow(() -> validator.getBookCopyIfExistInDB(1L));
    }

    @Test
    void GIVEN_no_exists_copy_id_WHEN_getBookCopyIfExistInDB_THEN_throw_EntityNotFoundException() {
        when(bookCopyRepository.findById(1L)).thenReturn(Optional.empty());

        //WHEN  //THEN
        assertThatThrownBy(() -> validator.getBookCopyIfExistInDB(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(BOOK_COPY_NOT_FOUND + 1L);
    }

    @Test
    void GIVEN_mismatched_book_ids_WHEN_controlOwnershipBookCopyAndBook_THEN_throw_IllegalArgumentException() {
        //GIVEN
        Book book = new Book();
        book.setId(2L);

        BookCopy copy = new BookCopy();
        copy.setBook(book);

        //WHEN  //THEN
        assertThatThrownBy(() -> validator.controlOwnershipBookCopyAndBook(1L, copy))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(NO_COPIES_AVAILABLE + 1L);
    }

    @Test
    void GIVEN_matching_book_ids_WHEN_controlOwnershipBookCopyAndBook_THEN_pass() {
        //GIVEN
        Book book = new Book();
        book.setId(1L);

        BookCopy copy = new BookCopy();
        copy.setBook(book);

        //WHEN  //THEN
        assertDoesNotThrow(() -> validator.controlOwnershipBookCopyAndBook(1L, copy));
    }

    @Test
    void GIVEN_book_with_no_copies_WHEN_validateExistsBookCopyFromBook_THEN_throw_IllegalArgumentException() {
        //GIVEN
        Book book = new Book();
        book.setCopies(Collections.emptyList());

        //WHEN  //THEN
        assertThatThrownBy(() -> validator.validateExistsBookCopyFromBook(book))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NO_COPIES_AVAILABLE);
    }

    @Test
    void GIVEN_book_with_copies_WHEN_validateExistsBookCopyFromBook_THEN_pass() {
        //GIVEN
        BookCopy copy = new BookCopy();
        Book book = new Book();
        book.setCopies(List.of(copy));

        //WHEN  //THEN
        assertDoesNotThrow(() -> validator.validateExistsBookCopyFromBook(book));
    }

    @Test
    void GIVEN_empty_list_WHEN_validateIfBookCopyListIsNotEmpty_THEN_throw_IllegalArgumentException() {
        //GIVEN empty list

        //WHEN  //THEN
        assertThatThrownBy(() -> validator.validateIfBookCopyListIsNotEmpty(Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NO_COPIES_AVAILABLE);
    }

    @Test
    void GIVEN_non_empty_list_WHEN_validateIfBookCopyListIsNotEmpty_THEN_pass() {
        //GIVEN no  empty list

        //WHEN  //THEN
        assertDoesNotThrow(() -> validator.validateIfBookCopyListIsNotEmpty(List.of(new BookCopy())));
    }

    @Test
    void GIVEN_empty_list_WHEN_validateIfBookCopyIsUnavailable_THEN_throw_IllegalArgumentException() {
        //GIVEN empty list

        //WHEN  //THEN
        assertThatThrownBy(() -> validator.validateIfBookCopyIsUnavailable(Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NO_UNAVAILABLE_COPIES);
    }

    @Test
    void GIVEN_non_empty_list_WHEN_validateIfBookCopyIsUnavailable_THEN_pass() {
        //GIVEN no empty list

        //WHEN  //THEN
        assertDoesNotThrow(() -> validator.validateIfBookCopyIsUnavailable(List.of(new BookCopy())));
    }

    @Test
    void GIVEN_empty_list_WHEN_validateIfBookCopyIsAvailable_THEN_throw_IllegalArgumentException() {
        //GIVEN empty list

        //WHEN  //THEN
        assertThatThrownBy(() -> validator.validateIfBookCopyIsAvailable(Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NO_AVAILABLE_COPIES);
    }

    @Test
    void GIVEN_non_empty_list_WHEN_validateIfBookCopyIsAvailable_THEN_pass() {
        //GIVEN no empty list

        //WHEN  //THEN
        assertDoesNotThrow(() -> validator.validateIfBookCopyIsAvailable(List.of(new BookCopy())));
    }
}
