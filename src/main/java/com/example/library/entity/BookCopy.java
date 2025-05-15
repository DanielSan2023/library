package com.example.library.entity;

import jakarta.persistence.*;

@Entity
public class BookCopy {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    //TODO exactly  sql command for Flyway migration
    private boolean available = true;
}
