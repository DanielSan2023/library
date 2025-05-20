package com.example.library.service.serviceimpl;

import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import com.example.library.dto.bookdtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {

    List<BookDtoSimple> getAllBooks();

    BookDtoSimple createBook(BookDtoResponse book);

    BookDtoFull getBookById(Long id);

    BookDtoFull updateBook(Long id, BookDtoUpdate book);

    void deleteBook(Long id);

    List<BookCopyDtoSimple> getCopiesByBookId(Long id);

    Page<BookDtoSimple> getPageBooks(Pageable pageable);

     void updateBookCopyAvailability(Long bookId, Boolean available);
}
