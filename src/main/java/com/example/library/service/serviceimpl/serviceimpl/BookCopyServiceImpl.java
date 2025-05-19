package com.example.library.service.serviceimpl.serviceimpl;

import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import com.example.library.dto.bookcopydtos.BookCopyDtoFull;
import com.example.library.entity.Book;
import com.example.library.entity.BookCopy;
import com.example.library.repository.BookCopyRepository;
import com.example.library.service.serviceimpl.BookCopyService;
import com.example.library.service.validation.BookFetcherValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class BookCopyServiceImpl implements BookCopyService {

    private final BookCopyRepository bookCopyRepository;
    private final ModelMapper modelMapper;
    private final BookFetcherValidator bookFetcherValidator;

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
    public BookCopyDtoFull updateAvailability(Long bookId, Long copyId, boolean available) {
        BookCopy bookCopy = bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new EntityNotFoundException("Book copy not found with id: " + copyId));

        if (!bookCopy.getBook().getId().equals(bookId)) {
            throw new IllegalArgumentException("Book copy does not belong to the specified book with id: " + bookId);
        }

        bookCopy.setAvailable(available);
        BookCopy updated = bookCopyRepository.save(bookCopy);

        return modelMapper.map(updated, BookCopyDtoFull.class);
    }

    //TODO add method for deleting bookCopy
}
