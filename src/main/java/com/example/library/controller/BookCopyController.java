package com.example.library.controller;

import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import com.example.library.dto.bookcopydtos.BookCopyDtoFull;
import com.example.library.dto.bookcopydtos.BookCopyDtoUpdate;
import com.example.library.service.serviceimpl.BookCopyService;
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

    @PostMapping("/{id}/copies")
    public ResponseEntity<BookCopyDtoSimple> addCopyToBook(@PathVariable Long id) {
        BookCopyDtoSimple createdBookCopy = bookCopyService.addCopyToBook(id);
        return new ResponseEntity<>(createdBookCopy, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/copies/{copyId}")
    public ResponseEntity<BookCopyDtoFull> updateBookCopyAvailability(@PathVariable Long id, @PathVariable Long copyId, @Valid @RequestBody BookCopyDtoUpdate bookCopyDto) {
        BookCopyDtoFull updatedBookCopy = bookCopyService.updateAvailability(id, copyId, bookCopyDto.getAvailable());
        return new ResponseEntity<>(updatedBookCopy, HttpStatus.OK);
    }
}
