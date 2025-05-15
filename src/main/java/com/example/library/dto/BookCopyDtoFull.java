package com.example.library.dto;

import com.example.library.entity.Book;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Data
@ToString
public class BookCopyDtoFull {
    private Long id;
    private Book book;
    private boolean available;
}
