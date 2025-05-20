package com.example.library.service.validation;

import com.example.library.entity.Book;
import com.example.library.repository.BookRepository;
import com.example.library.utility.BookConstants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class BookFetcherValidator {
    private final BookRepository bookRepository;

    public BookFetcherValidator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book getBookIfExistInDB(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BookConstants.BOOK_NOT_FOUND + id));
    }
}

