package com.example.library.dto.bookdtos;

import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDtoFull {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer publishedYear;
    private List<BookCopyDtoSimple> copies;
}
