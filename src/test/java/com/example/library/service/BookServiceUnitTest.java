package com.example.library.service;

import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import com.example.library.dto.bookdtos.BookDtoFull;
import com.example.library.dto.bookdtos.BookDtoResponse;
import com.example.library.dto.bookdtos.BookDtoSimple;
import com.example.library.dto.bookdtos.BookDtoUpdate;
import com.example.library.entity.Book;
import com.example.library.entity.BookCopy;
import com.example.library.repository.BookRepository;
import com.example.library.service.serviceimpl.serviceimpl.BookServiceImpl;
import com.example.library.service.validation.BookFetcherValidator;
import com.example.library.service.validation.BookValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookValidator bookValidator;

    @Mock
    private BookFetcherValidator bookFetcherValidator;

    private BookServiceImpl bookService;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        bookService = new BookServiceImpl(bookRepository, modelMapper, bookValidator, bookFetcherValidator);
    }

    @Test
    void GIVEN_exist_books_in_DB_WHEN_getAllBooks_THEN_return_mapped_BookDtoSimple_list() {
        // Given
        Book book1 = Book.builder()
                .id(1L)
                .title("Book One")
                .build();

        Book book2 = Book.builder()
                .id(2L)
                .title("Book Two")
                .build();

        BookDtoSimple dto1 = modelMapper.map(book1, BookDtoSimple.class);
        BookDtoSimple dto2 = modelMapper.map(book2, BookDtoSimple.class);

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        // When
        List<BookDtoSimple> result = bookService.getAllBooks();

        // Then
        assertThat(result)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(dto1, dto2));

        verify(bookRepository).findAll();
    }


    @Test
    void GIVEN_new_BookDto_WHEN_createBook_THEN_validate_save_return_BookDtoResponse() {
        // Given
        BookDtoResponse bookDto = BookDtoResponse.builder()
                .isbn("1234567890")
                .title("Test Title")
                .publishedYear(2024)
                .build();

        Book savedBook = Book.builder()
                .isbn("1234567890")
                .title("Test Title")
                .publishedYear(2024)
                .build();

        Book bookEntity = modelMapper.map(bookDto, Book.class);
        BookDtoResponse expectedDto = modelMapper.map(savedBook, BookDtoResponse.class);

        doNothing().when(bookValidator).validateIsbn(bookDto.getIsbn());
        doNothing().when(bookValidator).validatePublishedYear(bookDto.getPublishedYear());
        when(bookRepository.existsByTitle(bookDto.getTitle())).thenReturn(false);
        when(bookRepository.existsByIsbn(bookDto.getIsbn())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        // When
        BookDtoResponse result = bookService.createBook(bookDto);

        // Then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expectedDto);

        verify(bookValidator).validateIsbn(bookDto.getIsbn());
        verify(bookValidator).validatePublishedYear(bookDto.getPublishedYear());
        verify(bookRepository).existsByTitle(bookDto.getTitle());
        verify(bookRepository).existsByIsbn(bookDto.getIsbn());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void GIVEN_book_exists_WHEN_getBookById_THEN_return_BookDtoFull() {
        Long bookId = 1L;
        Book book = Book.builder()
                .title("Book")
                .build();

        when(bookFetcherValidator.getBookIfExistInDB(bookId)).thenReturn(book);

        BookDtoFull expected = modelMapper.map(book, BookDtoFull.class);

        BookDtoFull result = bookService.getBookById(bookId);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);

        verify(bookFetcherValidator).getBookIfExistInDB(bookId);
    }

    @Test
    void GIVEN_valid_update_WHEN_updateBook_THEN_validate_and_return_updated_BookDtoFull() {
        // Given
        Long bookId = 1L;
        Book existingBook = Book.builder()
                .id(bookId)
                .isbn("1234567890")
                .build();

        BookDtoUpdate updateDto = BookDtoUpdate.builder()
                .title("Updated Title")
                .isbn("1234567890")
                .publishedYear(2020)
                .build();


        Book updatedBook = new Book();
        updatedBook.setId(bookId);
        updatedBook.setTitle(updateDto.getTitle());
        updatedBook.setIsbn(updateDto.getIsbn());
        updatedBook.setPublishedYear(updateDto.getPublishedYear());

        BookDtoFull expectedDto = modelMapper.map(updatedBook, BookDtoFull.class);

        when(bookFetcherValidator.getBookIfExistInDB(bookId)).thenReturn(existingBook);
        when(bookRepository.save(existingBook)).thenReturn(updatedBook);

        // When
        BookDtoFull result = bookService.updateBook(bookId, updateDto);

        // Then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expectedDto);

        verify(bookValidator).validatePublishedYear(updateDto.getPublishedYear());
        verify(bookValidator).validateIsbn(updateDto.getIsbn());
        verify(bookValidator).validateIsbnConsistency(existingBook.getIsbn(), updateDto.getIsbn());
        verify(bookFetcherValidator).getBookIfExistInDB(bookId);
        verify(bookRepository).save(existingBook);
    }

    @Test
    void GIVEN_book_no_exist_WHEN_deleteBook_THEN_throwEntityNotFoundException() {
        Long nonExistId = 10L;
        when(bookRepository.existsById(nonExistId)).thenReturn(false);

        assertThatThrownBy(() -> bookService.deleteBook(nonExistId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Book not found with id: " + nonExistId);

        verify(bookRepository).existsById(nonExistId);
        verify(bookRepository, never()).deleteById(any());
    }

    @Test
    void GIVEN_book_exists_WHEN_deleteBook_THEN_deleteById_called() {
        Long existId = 5L;
        when(bookRepository.existsById(existId)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(existId);

        bookService.deleteBook(existId);

        verify(bookRepository).existsById(existId);
        verify(bookRepository).deleteById(existId);
    }

    @Test
    void GIVEN_book_with_copies_WHEN_getCopiesByBookId_THEN_return_CopyDtos_list() {
        Long bookId = 1L;
        Book book = new Book();

        BookCopy copy1 = new BookCopy();
        BookCopy copy2 = new BookCopy();
        book.setCopies(List.of(copy1, copy2));

        when(bookFetcherValidator.getBookIfExistInDB(bookId)).thenReturn(book);

        List<BookCopyDtoSimple> expectedList = book.getCopies().stream()
                .map(copy -> modelMapper.map(copy, BookCopyDtoSimple.class))
                .toList();

        List<BookCopyDtoSimple> result = bookService.getCopiesByBookId(bookId);

        assertThat(result)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedList);

        verify(bookFetcherValidator).getBookIfExistInDB(bookId);
    }

    @Test
    void GIVEN_existing_title_WHEN_createBook_THEN_throw_IllegalArgumentException() {
        // Given
        BookDtoResponse bookDto = BookDtoResponse.builder()
                .title("Existing Title")
                .isbn("1234567890")
                .publishedYear(2023)
                .build();

        doNothing().when(bookValidator).validateIsbn(bookDto.getIsbn());
        doNothing().when(bookValidator).validatePublishedYear(bookDto.getPublishedYear());
        when(bookRepository.existsByTitle(bookDto.getTitle())).thenReturn(true);

        // When / Then
        assertThatThrownBy(() -> bookService.createBook(bookDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Book with title is already exist in DB!");

        verify(bookRepository).existsByTitle(bookDto.getTitle());
        verify(bookRepository, never()).existsByIsbn(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void GIVEN_existing_isbn_WHEN_createBook_THEN_throw_IllegalArgumentException() {
        // Given
        BookDtoResponse bookDto = BookDtoResponse.builder()
                .title("New Title")
                .isbn("EXISTING_ISBN")
                .publishedYear(2022)
                .build();

        doNothing().when(bookValidator).validateIsbn(bookDto.getIsbn());
        doNothing().when(bookValidator).validatePublishedYear(bookDto.getPublishedYear());
        when(bookRepository.existsByTitle(bookDto.getTitle())).thenReturn(false);
        when(bookRepository.existsByIsbn(bookDto.getIsbn())).thenReturn(true);

        // When / Then
        assertThatThrownBy(() -> bookService.createBook(bookDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Book with ISBN is already exist in DB!");

        verify(bookRepository).existsByTitle(bookDto.getTitle());
        verify(bookRepository).existsByIsbn(bookDto.getIsbn());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void GIVEN_empty_bookDto_WHEN_checkForDuplicate_by_java_reflexion_THEN_no_exception_thrown() {
        // Given
        BookDtoResponse bookDto = new BookDtoResponse();

        when(bookRepository.existsByTitle(null)).thenReturn(false);
        when(bookRepository.existsByIsbn(null)).thenReturn(false);

        // When / Then
        assertThatCode(() -> {
            var method = BookServiceImpl.class.getDeclaredMethod("checkForDuplicate", BookDtoResponse.class);
            method.setAccessible(true);
            method.invoke(bookService, bookDto);
        }).doesNotThrowAnyException();

        verify(bookRepository).existsByTitle(null);
        verify(bookRepository).existsByIsbn(null);
    }
}



