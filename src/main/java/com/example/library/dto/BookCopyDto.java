package com.example.library.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class BookCopyDto {
    private Long id;
    private boolean available;
}
