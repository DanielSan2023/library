package com.example.library.dto.bookdtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDtoResponse {
    private String title;
    private String author;
    private String isbn;
    private Integer publishedYear;
}
