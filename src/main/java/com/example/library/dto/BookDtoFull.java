package com.example.library.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class BookDtoFull {
    private String title;
    private String author;
    private String isbn;
    private Integer publishedYear;
    private List<BookCopyDto> copies;
}
