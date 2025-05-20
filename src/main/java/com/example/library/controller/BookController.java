package com.example.library.controller;

import com.example.library.controller.utils.SortUtils;
import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import com.example.library.dto.bookdtos.BookDtoFull;
import com.example.library.dto.bookdtos.BookDtoResponse;
import com.example.library.dto.bookdtos.BookDtoSimple;
import com.example.library.dto.bookdtos.BookDtoUpdate;
import com.example.library.service.serviceimpl.BookService;
import com.example.library.utility.BookConstants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(
            summary = "Get all books",
            description = "Returns a list of all available books in the system.")
    @GetMapping()
    public ResponseEntity<List<BookDtoSimple>> getAllBooks() {
        List<BookDtoSimple> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @Operation(
            summary = "Create a new book",
            description = "Creates a new book based on request body.")
    @PostMapping()
    public ResponseEntity<BookDtoSimple> createBook(@Valid @RequestBody BookDtoResponse book) {
        BookDtoSimple createdBook = bookService.createBook(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get a book by ID",
            description = "Fetches detailed information about a specific book by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<BookDtoFull> getBookById(@PathVariable Long id) {
        BookDtoFull book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @Operation(
            summary = "Update a book",
            description = "Updates an existing book by ID using the provided request body data.")
    @PutMapping("/{id}")
    public ResponseEntity<BookDtoFull> updateBook(@PathVariable Long id, @Valid @RequestBody BookDtoUpdate book) {
        BookDtoFull updatedBook = bookService.updateBook(id, book);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a book and its copies",
            description = "Deletes a book from the database by its ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Get all copies of a book",
            description = "Returns a list of all physical copies of a specific book.")
    @GetMapping("/{id}/copies")
    public ResponseEntity<List<BookCopyDtoSimple>> getAllBookCopies(@PathVariable Long id) {
        List<BookCopyDtoSimple> bookCopies = bookService.getCopiesByBookId(id);
        return new ResponseEntity<>(bookCopies, HttpStatus.OK);
    }

    @Operation(
            summary = "Get books with pagination",
            description = "Returns a paginated list of books based on page, size, and sort parameters.")
    @GetMapping("/pageable")
    public ResponseEntity<Page<BookDtoSimple>> getBooksPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(required = false) String[] sort
    ) {
        Sort sorting = Sort.unsorted();
        List<Sort.Order> orders = SortUtils.parseSortParams(sort);
        if (!orders.isEmpty()) {
            sorting = Sort.by(orders);
        }
        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<BookDtoSimple> books = bookService.getPageBooks(pageable);
        return ResponseEntity.ok(books);
    }

    @Operation(
            summary = "Borrow a book copy",
            description = "Allows a user to borrow a specific copy of a book.")
    @PutMapping("/{id}/copies/borrow")
    public ResponseEntity<Void> borrowBookCopy(@PathVariable Long id) {
        bookService.updateBookCopyAvailability(id, BookConstants.BORROW_BOOK_COPY);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Return a book copy",
            description = "Allows a user to return a borrowed copy of a book.")
    @PutMapping("/{id}/copies/return")
    public ResponseEntity<Void> returnBookCopy(@PathVariable Long id) {
        bookService.updateBookCopyAvailability(id, BookConstants.RETURN_BOOK_COPY);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
