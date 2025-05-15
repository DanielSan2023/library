package com.example.library.controller;

import com.example.library.dto.BookCopyDtoSimple;
import com.example.library.dto.BookCopyDtoFull;
import com.example.library.dto.BookCopyDtoUpdate;
import com.example.library.service.serviceimpl.BookCopyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/books")
public class BookCopyController {

    private final BookCopyService bookCopyService;

    @PostMapping("/{id}/copies")
    public ResponseEntity<BookCopyDtoSimple> addCopyToBook(@PathVariable Long id) {
        BookCopyDtoSimple createdBookCopy = bookCopyService.addCopyToBook(id);
        return new ResponseEntity<>(createdBookCopy, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/copies/{copyId}")
    public ResponseEntity<BookCopyDtoFull> updateBookCopyAvailability(@PathVariable Long id, @PathVariable Long copyId, @RequestBody BookCopyDtoUpdate bookCopyDto) {
        BookCopyDtoFull updatedBookCopy = bookCopyService.updateAvailability(id, copyId, bookCopyDto.isAvailable());
        return new ResponseEntity<>(updatedBookCopy, HttpStatus.OK);
    }
}
