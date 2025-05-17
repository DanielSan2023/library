package com.example.library.dto.bookcopydtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookCopyDtoSimple {

    @NotBlank(message = "Book copy Id is required")
    private Long id;

    @NotNull(message = "Availability is required")
    private Boolean available;
}
