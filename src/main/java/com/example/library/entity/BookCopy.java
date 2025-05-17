package com.example.library.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "BOOK_COPY")
public class BookCopy {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Book book;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    //TODO exactly  sql command for Flyway migration
    private Boolean available = true;
}


