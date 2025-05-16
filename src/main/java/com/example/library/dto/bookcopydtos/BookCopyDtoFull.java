package com.example.library.dto.bookcopydtos;

import com.example.library.dto.bookdtos.BookDtoSimple;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookCopyDtoFull {
    private Long id;
    private BookDtoSimple book;
    private boolean available;
}
