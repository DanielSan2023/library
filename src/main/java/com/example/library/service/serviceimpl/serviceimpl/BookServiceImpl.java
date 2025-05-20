package com.example.library.service.serviceimpl.serviceimpl;

import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import com.example.library.dto.bookdtos.*;
import com.example.library.entity.Book;
import com.example.library.entity.BookCopy;
import com.example.library.repository.BookRepository;
import com.example.library.service.serviceimpl.BookCopyService;
import com.example.library.service.serviceimpl.BookService;
import com.example.library.service.validation.BookCopyFetcherValidator;
import com.example.library.service.validation.BookCopyValidator;
import com.example.library.service.validation.BookFetcherValidator;
import com.example.library.service.validation.BookValidator;
import com.example.library.utility.BookConstants;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.library.utility.BookConstants.*;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final BookValidator bookValidator;
    private final BookCopyValidator bookCopyValidator;
    private final BookFetcherValidator bookFetcherValidator;
    private final BookCopyFetcherValidator bookCopyFetcherValidator;
    private final BookCopyService bookCopyService;

    @Override
    public List<BookDtoSimple> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(book -> modelMapper.map(book, BookDtoSimple.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDtoSimple createBook(BookDtoResponse bookDto) {
        bookValidator.validateIsbn(bookDto.getIsbn());
        bookValidator.validatePublishedYear(bookDto.getPublishedYear());

        checkForDuplicate(bookDto);

        Book newBook = modelMapper.map(bookDto, Book.class);
        Book savedBook = bookRepository.save(newBook);

        return modelMapper.map(savedBook, BookDtoSimple.class);
    }

    @Override
    @Transactional(readOnly = true)
    public BookDtoFull getBookById(Long id) {
        Book book = bookFetcherValidator.getBookIfExistInDB(id);

        return modelMapper.map(book, BookDtoFull.class);
    }

    @Override
    @Transactional
    public BookDtoFull updateBook(Long id, BookDtoUpdate bookDto) {
        bookValidator.validatePublishedYear(bookDto.getPublishedYear());
        bookValidator.validateIsbn(bookDto.getIsbn());

        Book book = bookFetcherValidator.getBookIfExistInDB(id);
        bookValidator.validateIsbnConsistency(book.getIsbn(), bookDto.getIsbn());
        modelMapper.map(bookDto, book);

        Book updatedBook = bookRepository.save(book);
        return modelMapper.map(updatedBook, BookDtoFull.class);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException(BOOK_COPY_NOT_FOUND + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookCopyDtoSimple> getCopiesByBookId(Long id) {
        Book book = bookFetcherValidator.getBookIfExistInDB(id);

        return book.getCopies().stream()
                .map(bookCopy -> modelMapper.map(bookCopy, BookCopyDtoSimple.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<BookDtoSimple> getPageBooks(Pageable pageable) {
        Page<Book> booksPage = bookRepository.findAll(pageable);
        return booksPage.map(book -> modelMapper.map(book, BookDtoSimple.class));
    }

    private void checkForDuplicate(BookDtoResponse bookDto) {  //TODO refactor to BookValidator class
        if (bookRepository.existsByTitle(bookDto.getTitle())) {
            throw new IllegalArgumentException(DUPLICATE_TITLE_EXCEPTION);
        }
        if (bookRepository.existsByIsbn(bookDto.getIsbn())) {
            throw new IllegalArgumentException(DUPLICATE_ISBN_EXCEPTION);
        }
    }

    @Override
    public void updateBookCopyAvailability(Long bookId, Boolean available) {
        Book book = bookFetcherValidator.getBookIfExistInDB(bookId);
        bookCopyFetcherValidator.validateExistsBookCopyFromBook(book);

        List<BookCopy> allBookCopies = book.getCopies();
        bookCopyFetcherValidator.validateIfBookCopyListIsNotEmpty(allBookCopies);

        List<BookCopy> filteredCopies = allBookCopies.stream()
                .filter(copy -> Boolean.valueOf(!available).equals(copy.getAvailable()))
                .toList();

        bookCopyValidator.validateBookCopyAvailability(filteredCopies);

        if (available) {
            bookCopyFetcherValidator.validateIfBookCopyIsUnavailable(filteredCopies);
        } else {
            bookCopyFetcherValidator.validateIfBookCopyIsAvailable(filteredCopies);
        }

        BookCopy targetBookCopy = filteredCopies.get(0);
        bookCopyService.updateAvailability(bookId, targetBookCopy.getId(), available ? BookConstants.AVAILABLE : BookConstants.UNAVAILABLE);
    }
}
