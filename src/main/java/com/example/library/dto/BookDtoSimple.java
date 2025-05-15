package com.example.library.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class BookDtoSimple {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer publishedYear;
}
