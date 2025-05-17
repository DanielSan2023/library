package com.example.library.dto.bookcopydtos;

import com.example.library.dto.bookdtos.BookDtoSimple;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookCopyDtoFull {
    private Long id;

    @NotBlank(message = "Book is required")
    private BookDtoSimple book;

    @NotNull(message = "Availability is required")
    private Boolean available;
}
