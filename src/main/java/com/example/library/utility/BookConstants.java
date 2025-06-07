package com.example.library.utility;

public class BookConstants {
    public static final String BOOK_NOT_FOUND = "Book not found with id: ";
    public static final String BOOK_NOT_FOUND_TEST = "Book not found ";
    public static final String DUPLICATE_TITLE_EXCEPTION = "Book with title is already exist in DB!";
    public static final String DUPLICATE_ISBN_EXCEPTION = "Book with ISBN is already exist in DB!";
    public static final String ISBN_MISMATCH_EXCEPTION = "Book ISBN mismatch: existing = '%s', requested = '%s'";

    public static final String BOOK_COPY_NOT_FOUND = "Book copy not found with id: ";
    public static final String NO_COPIES_AVAILABLE = "No copies available for this book: ";
    public static final String NO_MATCHING_BOOK_COPIES = "No matching book copies found for availability change.";

    public static final String NO_AVAILABLE_COPIES = "No available copies found to borrow.";
    public static final String NO_UNAVAILABLE_COPIES = "No unavailable copies found to return.";

    public static final String ISBN_CANNOT_BE_NULL = "ISBN cannot be null.";
    public static final String ISBN_MISMATCH_MATCH = "Book ISBN mismatch";
    public static final String INVALID_ISBN_FORMAT = "Invalid ISBN format.";
    public static final String INVALID_PUBLISHED_YEAR = "Invalid published year.";
    public static final String DEFAULT_CUSTOMER_ROLE = "USER";

    public static final boolean UNAVAILABLE = false;
    public static final boolean AVAILABLE = true;

    public static final boolean BORROW_BOOK_COPY = false;
    public static final boolean RETURN_BOOK_COPY = true;


    public static final int MIN_TITLE_LENGTH = 10;
    public static final int MAX_TITLE_LENGTH = 13;
    public static final int MIN_PUBLISHED_YEAR_BOOK = 1443;


}
