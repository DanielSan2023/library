package com.example.library.dto.bookdtos;

import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import lombok.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDtoFull {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @NotNull(message = "Published year is required")
    private Integer publishedYear;

    private List<BookCopyDtoSimple> copies;
}
