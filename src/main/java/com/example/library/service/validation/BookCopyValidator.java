package com.example.library.service.validation;

import com.example.library.entity.BookCopy;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.library.utility.BookConstants.NO_MATCHING_BOOK_COPIES;

@Component
public class BookCopyValidator {

    public void validateBookCopyAvailability(List<BookCopy> bookCopies) {
        if (bookCopies.isEmpty()) {
            throw new IllegalArgumentException(NO_MATCHING_BOOK_COPIES);
        }
    }
}
