package com.example.library.dto.bookcopydtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookCopyDtoUpdate {

    @NotNull(message = "Availability is required")
    private Boolean available;
}
