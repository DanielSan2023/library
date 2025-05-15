package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface BookRepository extends JpaRepository<Book, Long> {
    //TODO add book to the repository
    //TODO find book by id
    //TODO find all books
    //TODO delete book by id
    //TODO update book by id
}
