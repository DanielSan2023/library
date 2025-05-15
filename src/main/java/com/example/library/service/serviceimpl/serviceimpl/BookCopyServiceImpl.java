package com.example.library.service.serviceimpl.serviceimpl;

import com.example.library.dto.BookCopyDtoSimple;
import com.example.library.dto.BookCopyDtoFull;
import com.example.library.entity.Book;
import com.example.library.entity.BookCopy;
import com.example.library.repository.BookCopyRepository;
import com.example.library.repository.BookRepository;
import com.example.library.service.serviceimpl.BookCopyService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookCopyServiceImpl implements BookCopyService {

    private final BookCopyRepository bookCopyRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public BookCopyServiceImpl(BookCopyRepository bookCopyRepository, BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookCopyRepository = bookCopyRepository;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public BookCopyDtoSimple addCopyToBook(Long bookId) {
        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));

            BookCopy bookCopy = new BookCopy();
            bookCopy.setBook(book);
            bookCopy.setAvailable(true);

            BookCopy savedBookCopy = bookCopyRepository.save(bookCopy);

            return modelMapper.map(savedBookCopy, BookCopyDtoSimple.class);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException("Unexpected error occurred while adding book copy", e);
        }
    }

    @Override
    @Transactional
    public BookCopyDtoFull updateAvailability(Long bookId, Long copyId, boolean available) {
        try {
            BookCopy bookCopy = bookCopyRepository.findById(copyId)
                    .orElseThrow(() -> new EntityNotFoundException("Book copy not found with id: " + copyId));

            if (!bookCopy.getBook().getId().equals(bookId)) {
                throw new IllegalArgumentException("Copy does not belong to the specified book with id: " + bookId);
            }

            bookCopy.setAvailable(available);
            BookCopy updated = bookCopyRepository.save(bookCopy);

            return modelMapper.map(updated, BookCopyDtoFull.class);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException("Unexpected error occurred while updating availability", e);
        }
    }
}
