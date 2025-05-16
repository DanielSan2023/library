package com.example.library.dto.bookdtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDtoUpdate {
    private String title;
    private Integer publishedYear;
}
