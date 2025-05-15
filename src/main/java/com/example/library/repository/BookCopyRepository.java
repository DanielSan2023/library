package com.example.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface BookCopyRepository extends JpaRepository {
    //TODO add book copy to the repository
    //TODO update book copy by id
}

