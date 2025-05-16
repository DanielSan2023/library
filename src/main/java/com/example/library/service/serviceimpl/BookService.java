package com.example.library.service.serviceimpl;

import com.example.library.dto.*;

import java.util.List;

public interface BookService {

    List<BookDtoSimple> getAllBooks();

    BookDtoResponse createBook(BookDtoResponse book);

    BookDtoFull getBookById(Long id);

    BookDtoFull updateBook(Long id, BookDtoUpdate book);

    void deleteBook(Long id);

    List<BookCopyDtoSimple> getCopiesByBookId(Long id);
}
