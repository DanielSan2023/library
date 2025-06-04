package com.example.library.service.service;

import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import com.example.library.dto.bookcopydtos.BookCopyDtoFull;

public interface BookCopyService {

     BookCopyDtoSimple addCopyToBook(Long bookId);

     BookCopyDtoFull updateAvailability(Long bookId, Long copyId, Boolean available);
}
