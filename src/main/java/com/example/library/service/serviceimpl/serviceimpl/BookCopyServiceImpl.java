package com.example.library.service.serviceimpl.serviceimpl;

import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import com.example.library.dto.bookcopydtos.BookCopyDtoFull;
import com.example.library.entity.Book;
import com.example.library.entity.BookCopy;
import com.example.library.repository.BookCopyRepository;
import com.example.library.service.serviceimpl.BookCopyService;
import com.example.library.service.validation.BookCopyFetcherValidator;
import com.example.library.service.validation.BookFetcherValidator;
import com.example.library.utility.BookConstants;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class BookCopyServiceImpl implements BookCopyService {

    private final BookCopyRepository bookCopyRepository;
    private final ModelMapper modelMapper;
    private final BookFetcherValidator bookFetcherValidator;
    private final BookCopyFetcherValidator bookCopyFetcherValidator;

    @Override
    @Transactional
    public BookCopyDtoSimple addCopyToBook(Long bookId) {
        Book book = bookFetcherValidator.getBookIfExistInDB(bookId);

        BookCopy bookCopy = BookCopy.builder()
                .book(book)
                .available(true)
                .build();

        BookCopy savedBookCopy = bookCopyRepository.save(bookCopy);

        return modelMapper.map(savedBookCopy, BookCopyDtoSimple.class);
    }

    @Override
    @Transactional
    public BookCopyDtoFull updateAvailability(Long bookId, Long copyId, Boolean available) {
        BookCopy bookCopy = bookCopyFetcherValidator.getBookCopyIfExistInDB(copyId);

        bookCopyFetcherValidator.controlOwnershipBookCopyAndBook(bookId, bookCopy);

        bookCopy.setAvailable(available);
        BookCopy updated = bookCopyRepository.save(bookCopy);

        return modelMapper.map(updated, BookCopyDtoFull.class);
    }
    //TODO add method for deleting bookCopy
}
