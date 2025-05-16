package com.example.library.dto.bookcopydtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookCopyDtoSimple {
    private Long id;
    private boolean available;
}
