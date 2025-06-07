package com.example.library.controller;

import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import com.example.library.dto.bookcopydtos.BookCopyDtoFull;
import com.example.library.dto.bookcopydtos.BookCopyDtoUpdate;
import com.example.library.service.service.BookCopyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookCopyController {
    private final BookCopyService bookCopyService;

    public BookCopyController(BookCopyService bookCopyService) {
        this.bookCopyService = bookCopyService;
    }

    @Operation(
            summary = "Add a new book copy",
            description = "Creates a new physical copy for the specified book.")
    @PostMapping("/{id}/copies")
    public ResponseEntity<BookCopyDtoSimple> addCopyToBook(@PathVariable Long id) {
        BookCopyDtoSimple createdBookCopy = bookCopyService.addCopyToBook(id);
        return new ResponseEntity<>(createdBookCopy, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update a book copy",
            description = "Updates a specific copy of a book (e.g., condition or availability).")
    @PutMapping("/{id}/copies/{copyId}")
    public ResponseEntity<BookCopyDtoFull> updateBookCopyAvailability(@PathVariable Long id, @PathVariable Long copyId, @Valid @RequestBody BookCopyDtoUpdate bookCopyDto) {
        BookCopyDtoFull updatedBookCopy = bookCopyService.updateAvailability(id, copyId, bookCopyDto.getAvailable());
        return new ResponseEntity<>(updatedBookCopy, HttpStatus.OK);
    }

    //TODO add method for deleting bookCopy
}
