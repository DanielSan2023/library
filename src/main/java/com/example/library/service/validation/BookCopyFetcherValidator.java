package com.example.library.service.validation;

import com.example.library.entity.Book;
import com.example.library.entity.BookCopy;
import com.example.library.repository.BookCopyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.library.utility.BookConstants.*;

@Component
public class BookCopyFetcherValidator {
    private final BookCopyRepository bookCopyRepository;

    public BookCopyFetcherValidator(BookCopyRepository bookCopyRepository) {
        this.bookCopyRepository = bookCopyRepository;
    }

    public BookCopy getBookCopyIfExistInDB(Long id) {
        return bookCopyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_COPY_NOT_FOUND + id));
    }

    public void controlOwnershipBookCopyAndBook(Long bookId, BookCopy bookCopy) {
        if (!bookCopy.getBook().getId().equals(bookId)) {
            throw new IllegalArgumentException(NO_COPIES_AVAILABLE + bookId);
        }
    }

    public void validateExistsBookCopyFromBook(Book book) {
        if (book.getCopies().isEmpty()) {
            throw new IllegalArgumentException(NO_COPIES_AVAILABLE);
        }
    }

    public void validateIfBookCopyListIsNotEmpty(List<BookCopy> availableCopies) {
        if (availableCopies.isEmpty()) {
            throw new IllegalArgumentException(NO_COPIES_AVAILABLE);
        }
    }

    public void validateIfBookCopyIsUnavailable(List<BookCopy> unavailableCopies) {
        if (unavailableCopies.isEmpty()) {
            throw new IllegalArgumentException(NO_UNAVAILABLE_COPIES);
        }
    }

    public void validateIfBookCopyIsAvailable(List<BookCopy> availableCopies) {
        if (availableCopies.isEmpty()) {
            throw new IllegalArgumentException(NO_AVAILABLE_COPIES);
        }
    }
}