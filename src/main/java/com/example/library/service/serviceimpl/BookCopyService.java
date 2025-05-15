package com.example.library.service.serviceimpl;

import com.example.library.dto.BookCopyDtoSimple;
import com.example.library.dto.BookCopyDtoFull;

public interface BookCopyService {

     BookCopyDtoSimple addCopyToBook(Long bookId);

     BookCopyDtoFull updateAvailability(Long bookId, Long copyId, boolean available);
}
