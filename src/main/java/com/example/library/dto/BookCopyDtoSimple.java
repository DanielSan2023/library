package com.example.library.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Data
@ToString
public class BookCopyDtoSimple {
    private Long id;
    private boolean available;
}
