package com.example.library.service.serviceimpl;

import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import com.example.library.dto.bookdtos.*;

import java.util.List;

public interface BookService {

    List<BookDtoSimple> getAllBooks();

    BookDtoResponseFull createBook(BookDtoResponse book);

    BookDtoFull getBookById(Long id);

    BookDtoFull updateBook(Long id, BookDtoUpdate book);

    void deleteBook(Long id);

    List<BookCopyDtoSimple> getCopiesByBookId(Long id);
}
