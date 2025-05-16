package com.example.library.repository;

import com.example.library.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {

    void deleteByBookId(Long id);
}

