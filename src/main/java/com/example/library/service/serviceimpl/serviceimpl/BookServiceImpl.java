package com.example.library.service.serviceimpl.serviceimpl;

import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import com.example.library.dto.bookdtos.BookDtoFull;
import com.example.library.dto.bookdtos.BookDtoResponse;
import com.example.library.dto.bookdtos.BookDtoSimple;
import com.example.library.dto.bookdtos.BookDtoUpdate;
import com.example.library.entity.Book;
import com.example.library.repository.BookCopyRepository;
import com.example.library.repository.BookRepository;
import com.example.library.service.serviceimpl.BookService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<BookDtoSimple> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(book -> modelMapper.map(book, BookDtoSimple.class))
                .collect(Collectors.toList());
    }

    @Override
    public BookDtoResponse createBook(BookDtoResponse book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {           //TODO check if doesn't exist book with ISBN
            throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " already exists");
        }
        Book newBook = modelMapper.map(book, Book.class);
        Book savedBook = bookRepository.save(newBook);

        return modelMapper.map(savedBook, BookDtoResponse.class);
    }

    @Override
    public BookDtoFull getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        return modelMapper.map(book, BookDtoFull.class);
    }

    @Override
    public BookDtoFull updateBook(Long id, BookDtoUpdate bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));

        modelMapper.map(bookDto, book);

        Book updatedBook = bookRepository.save(book);
        return modelMapper.map(updatedBook, BookDtoFull.class);
    }

    @Transactional
    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found with id: " + id);
        }
        bookCopyRepository.deleteByBookId(id);
        bookRepository.deleteById(id);//TODO check if book has copies
    }

    @Override
    public List<BookCopyDtoSimple> getCopiesByBookId(Long id) {  //TODO is doesn't exist copies of Book create exception or  message to FE
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));

        return book.getCopies().stream()
                .map(bookCopy -> modelMapper.map(bookCopy, BookCopyDtoSimple.class))
                .collect(Collectors.toList());
    }
}
