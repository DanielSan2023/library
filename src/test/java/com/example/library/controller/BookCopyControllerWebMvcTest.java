package com.example.library.controller;

import com.example.library.dto.bookcopydtos.BookCopyDtoFull;
import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import com.example.library.dto.bookcopydtos.BookCopyDtoUpdate;
import com.example.library.service.service.BookCopyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookCopyController.class)
public class BookCopyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookCopyService bookCopyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void GIVEN_copyUpdateDto_WHEN_updateCopyAvailability_THEN_return_UpdatedCopy() throws Exception {
        //GIVEN
        Long bookId = 1L;
        Long copyId = 2L;

        BookCopyDtoUpdate dto = new BookCopyDtoUpdate();
        dto.setAvailable(false);

        BookCopyDtoFull updatedCopy = new BookCopyDtoFull();
        updatedCopy.setId(copyId);
        updatedCopy.setAvailable(false);

        when(bookCopyService.updateAvailability(eq(bookId), eq(copyId), eq(false))).thenReturn(updatedCopy);

        //WHEN //THEN
        mockMvc.perform(put("/api/books/{id}/copies/{copyId}", bookId, copyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(copyId))
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    void GIVEN_bookId_WHEN_addCopyToBook_THEN_return_CreatedCopy() throws Exception {
        //GIVEN
        Long bookId = 1L;

        BookCopyDtoSimple createdCopy = BookCopyDtoSimple.builder()
                .id(bookId)
                .available(true)
                .build();

        when(bookCopyService.addCopyToBook(eq(bookId))).thenReturn(createdCopy);

        //WHEN //THEN
        mockMvc.perform(post("/api/books/{id}/copies", bookId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void GIVEN_no_exists_CopyId_WHEN_updateAvailability_THEN_return_NotFound_Status() throws Exception {
        //GIVEN
        Long bookId = 1L;
        Long copyId = 999L;

        BookCopyDtoUpdate dto = new BookCopyDtoUpdate();
        dto.setAvailable(true);

        when(bookCopyService.updateAvailability(eq(bookId), eq(copyId), anyBoolean()))
                .thenThrow(new EntityNotFoundException("Book copy not found with id: " + copyId));

        //WHEN //THEN
        mockMvc.perform(put("/api/books/{id}/copies/{copyId}", bookId, copyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Book copy not found with id: " + copyId));
    }
}
