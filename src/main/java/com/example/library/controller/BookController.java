package com.example.library.controller;

import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import com.example.library.dto.bookdtos.BookDtoFull;
import com.example.library.dto.bookdtos.BookDtoResponse;
import com.example.library.dto.bookdtos.BookDtoSimple;
import com.example.library.dto.bookdtos.BookDtoUpdate;
import com.example.library.service.serviceimpl.BookService;
import jakarta.validation.Valid;
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

    @GetMapping()
    public ResponseEntity<List<BookDtoSimple>> getAllBooks() {
        List<BookDtoSimple> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<BookDtoResponse> createBook(@Valid @RequestBody BookDtoResponse book) {
        BookDtoResponse createdBook = bookService.createBook(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDtoFull> getBookById(@PathVariable Long id) {
        BookDtoFull book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDtoFull> updateBook(@PathVariable Long id, @Valid @RequestBody BookDtoUpdate book) {
        BookDtoFull updatedBook = bookService.updateBook(id, book);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/copies")
    public ResponseEntity<List<BookCopyDtoSimple>> getAllBookCopies(@PathVariable Long id) {
        List<BookCopyDtoSimple> bookCopies = bookService.getCopiesByBookId(id);
        return new ResponseEntity<>(bookCopies, HttpStatus.OK);
    }
}
