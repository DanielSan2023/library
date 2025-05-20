package com.example.library.controller;

import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import com.example.library.dto.bookdtos.*;
import com.example.library.service.serviceimpl.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static com.example.library.utility.BookConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void GIVEN_booksExist_WHEN_getAllBooks_THEN_returnListOfBooks() throws Exception {
        //GIVEN
        List<BookDtoSimple> books = List.of(
                new BookDtoSimple(1L, "Effective Java", "Joshua Bloch", "978-0134685991", 2018),
                new BookDtoSimple(2L, "Clean Code", "Robert C. Martin", "978-0132350884", 2008)
        );

        when(bookService.getAllBooks()).thenReturn(books);

        //WHEN //THEN
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Effective Java"))
                .andExpect(jsonPath("$[0].author").value("Joshua Bloch"))
                .andExpect(jsonPath("$[0].isbn").value("978-0134685991"))
                .andExpect(jsonPath("$[0].publishedYear").value(2018))
                .andExpect(jsonPath("$[1].title").value("Clean Code"));
    }

    @Test
    void GIVEN_validBookDto_WHEN_addNewBook_THEN_returnCreatedBook() throws Exception {
        //GIVEN
        BookDtoResponse inputDto = BookDtoResponse.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .publishedYear(2008)
                .build();

        BookDtoSimple returnedDto = BookDtoSimple.builder()
                .id(10L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .publishedYear(2008)
                .build();

        returnedDto.setId(10L);
        returnedDto.setTitle("Clean Code");
        returnedDto.setAuthor("Robert C. Martin");
        returnedDto.setIsbn("978-0132350884");
        returnedDto.setPublishedYear(2008);

        when(bookService.createBook(any(BookDtoResponse.class))).thenReturn(returnedDto);

        //WHEN //THEN
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert C. Martin"))
                .andExpect(jsonPath("$.isbn").value("978-0132350884"))
                .andExpect(jsonPath("$.publishedYear").value(2008));
    }

    @Test
    void GIVEN_existingBookId_WHEN_getBookById_THEN_returnBookWithCopies() throws Exception {
        //GIVEN
        BookDtoFull book = new BookDtoFull();
        book.setId(1L);
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setIsbn("978-0134685991");
        book.setPublishedYear(2018);
        book.setCopies(List.of(
                new BookCopyDtoSimple(1L, true),
                new BookCopyDtoSimple(2L, false)
        ));

        when(bookService.getBookById(eq(1L))).thenReturn(book);

        //WHEN //THEN
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Effective Java"))
                .andExpect(jsonPath("$.author").value("Joshua Bloch"))
                .andExpect(jsonPath("$.isbn").value("978-0134685991"))
                .andExpect(jsonPath("$.publishedYear").value(2018))
                .andExpect(jsonPath("$.copies.length()").value(2))
                .andExpect(jsonPath("$.copies[0].id").value(1))
                .andExpect(jsonPath("$.copies[0].available").value(true))
                .andExpect(jsonPath("$.copies[1].id").value(2))
                .andExpect(jsonPath("$.copies[1].available").value(false));
    }

    @Test
    void GIVEN_updateBookDto_WHEN_updateBook_THEN_return_updatedBook() throws Exception {
        //GIVEN
        Long bookId = 1L;

        BookDtoUpdate updateDto = BookDtoUpdate.builder()
                .title("Effective Java (3rd Edition)")
                .isbn("978-0134685991")
                .publishedYear(2018)
                .build();

        BookDtoFull updatedBook = BookDtoFull.builder()
                .id(bookId)
                .title("Effective Java (3rd Edition)")
                .author("Joshua Bloch")
                .isbn("978-0134685991")
                .publishedYear(2018)
                .build();

        when(bookService.updateBook(eq(bookId), any(BookDtoUpdate.class))).thenReturn(updatedBook);

        //WHEN //THEN
        mockMvc.perform(put("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Effective Java (3rd Edition)"))
                .andExpect(jsonPath("$.author").value("Joshua Bloch"))
                .andExpect(jsonPath("$.isbn").value("978-0134685991"))
                .andExpect(jsonPath("$.publishedYear").value(2018));
    }

    @Test
    void GIVEN_bookWithCopies_WHEN_getCopies_THEN_returnListOfCopies() throws Exception {
        //GIVEN
        Long bookId = 1L;

        BookCopyDtoSimple copy1 = new BookCopyDtoSimple(1L, true);
        BookCopyDtoSimple copy2 = new BookCopyDtoSimple(2L, false);

        when(bookService.getCopiesByBookId(eq(bookId))).thenReturn(Arrays.asList(copy1, copy2));

        //WHEN //THEN
        mockMvc.perform(get("/api/books/{id}/copies", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].available").value(false));
    }

    @Test
    void GIVEN_validBookId_WHEN_borrowBookCopy_THEN_return_No_Content_Status() throws Exception {
        // GIVEN
        Long bookId = 1L;

        // WHEN  // THEN
        mockMvc.perform(put("/api/books/{id}/copies/borrow", bookId))
                .andExpect(status().isNoContent());
    }

    @Test
    void GIVEN_validBookId_WHEN_returnBookCopy_THEN_return_No_Content_Status() throws Exception {
        // GIVEN
        Long bookId = 1L;

        // WHEN // THEN
        mockMvc.perform(put("/api/books/{id}/copies/return", bookId))
                .andExpect(status().isNoContent());
    }

    @Test
    void GIVEN_no_exists_BookId_WHEN_borrowBookCopy_THEN_return_Not_Found_Status() throws Exception {
        // GIVEN
        Long bookId = 999L;

        org.mockito.Mockito.doThrow(new com.example.library.exception.BookNotFoundException("Book not found"))
                .when(bookService).updateBookCopyAvailability(eq(bookId), eq(BORROW_BOOK_COPY));

        // WHEN // THEN
        mockMvc.perform(put("/api/books/{id}/copies/borrow", bookId))
                .andExpect(status().isNotFound());
    }

    @Test
    void GIVEN_no_exists_BookId__WHEN_returnBookCopy_THEN_return_Not_Found_Status() throws Exception {
        // GIVEN
        Long bookId = 999L;

        org.mockito.Mockito.doThrow(new com.example.library.exception.BookNotFoundException("Book not found"))
                .when(bookService).updateBookCopyAvailability(eq(bookId), eq(RETURN_BOOK_COPY));

        // WHEN // THEN
        mockMvc.perform(put("/api/books/{id}/copies/return", bookId))
                .andExpect(status().isNotFound());
    }
}
