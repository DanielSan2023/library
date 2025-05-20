package com.example.library.utility;

public class BookConstants {
    public static final String BOOK_NOT_FOUND = "Book not found with id: ";
    public static final String BOOK_COPY_NOT_FOUND = "Book copy not found with id: ";
    public static final String BOOK_COPY_NOT_BELONG_TO_BOOK = "Book copy does not belong to the specified book with id: ";
    public static final String NO_COPIES_AVAILABLE = "No copies available for this book: ";

    public static final boolean UNAVAILABLE = false;
    public static final boolean AVAILABLE = true;

    public static final boolean BORROW_BOOK_COPY = false;
    public static final boolean RETURN_BOOK_COPY = true;

    public static final String NO_AVAILABLE_COPIES = "No available copies found to borrow.";
    public static final String NO_UNAVAILABLE_COPIES = "No unavailable copies found to return.";

    public static final int MIN_TITLE_LENGTH = 10;
    public static final int MAX_TITLE_LENGTH = 13;
    public static final int MIN_PUBLISHED_YEAR_BOOK = 1443;

    public static final String ISBN_CANNOT_BE_NULL = "ISBN cannot be null.";
    public static final String INVALID_ISBN_FORMAT = "Invalid ISBN format.";
    public static final String INVALID_PUBLISHED_YEAR = "Invalid published year.";

}
