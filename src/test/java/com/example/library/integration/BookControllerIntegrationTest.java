package com.example.library.integration;

import com.example.library.LibraryApplication;
import com.example.library.dto.bookcopydtos.BookCopyDtoSimple;
import com.example.library.dto.bookcopydtos.BookCopyDtoUpdate;
import com.example.library.dto.bookdtos.BookDtoResponse;
import com.example.library.dto.bookdtos.BookDtoResponseFull;
import com.example.library.entity.Book;
import com.example.library.integration.config.TestcontainersConfiguration;
import com.example.library.repository.BookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {LibraryApplication.class, TestcontainersConfiguration.class})
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    @Autowired
    private Flyway flyway;

    @BeforeEach
    void cleanDatabase() {
        flyway.clean();
        flyway.migrate();
        bookRepository.deleteAll();
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private Environment environment;

    @Test
    void printActiveProfiles() {
        System.out.println("Active profiles: " + Arrays.toString(environment.getActiveProfiles()));
        System.out.println("Datasource URL: " + environment.getProperty("spring.datasource.url"));
    }

    private static Long bookId;
    private static Long copyId1;
    private static Long copyId2;

    @Test
    @DisplayName("should retrieve simply book by bookId")
    void testSaveAndFindBook() {
        Book book = Book.builder()
                .isbn("978-3-16-148410-0")
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .publishedYear(1925)
                .build();

        Book createdBook = bookRepository.save(book);

        assertThat(createdBook.getId()).isNotNull();
        Optional<Book> foundBook = bookRepository.findById(createdBook.getId());

        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getIsbn()).isEqualTo("978-3-16-148410-0");
        assertThat(foundBook.get().getTitle()).isEqualTo("The Great Gatsby");
        assertThat(foundBook.get().getAuthor()).isEqualTo("F. Scott Fitzgerald");
        assertThat(foundBook.get().getPublishedYear()).isEqualTo(1925);
    }

    @Test
    @DisplayName("should retrieve book copies by their bookId")
    void createBook_thenAddCopies() throws Exception {

        //CREATE BOOK
        BookDtoResponse newBook = new BookDtoResponse();
        newBook.setTitle("Effective Java");
        newBook.setAuthor("Joshua Bloch");
        newBook.setIsbn("9780134685991");
        newBook.setPublishedYear(2018);

        String bookResponse = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookDtoResponseFull createdBook = objectMapper.readValue(bookResponse, BookDtoResponseFull.class);
        bookId = createdBook.getId();
        assertThat(bookId).isNotNull();
        assertThat(createdBook.getIsbn()).isEqualTo("9780134685991");

        //ADD COPIES to BOOK
        String copyResponse1 = mockMvc.perform(post("/api/books/{id}/copies", bookId))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookCopyDtoSimple copy1 = objectMapper.readValue(copyResponse1, BookCopyDtoSimple.class);
        copyId1 = copy1.getId();
        assertThat(copyId1).isNotNull();

        String copyResponse2 = mockMvc.perform(post("/api/books/{id}/copies", bookId))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookCopyDtoSimple copy2 = objectMapper.readValue(copyResponse2, BookCopyDtoSimple.class);
        copyId2 = copy2.getId();
        assertThat(copyId2).isNotNull();

        //GET BOOK COPIES BY BOOK ID
        String copiesResponse = mockMvc.perform(get("/api/books/{id}/copies", bookId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookCopyDtoSimple[] copies = objectMapper.readValue(copiesResponse, BookCopyDtoSimple[].class);
        assertThat(copies.length).isEqualTo(2);
        assertThat(Arrays.stream(copies).map(BookCopyDtoSimple::getId).toArray(Long[]::new))
                .contains(copyId1, copyId2);
    }

    @Test
    @DisplayName("should handle full book flow: create, add copies, verify, update availability of book copies, and delete.")
    void fullFlow_createBook_addCopies_verify_update_delete() throws Exception {

        // CREATE BOOK
        BookDtoResponse newBook = new BookDtoResponse();
        newBook.setTitle("Clean Code");
        newBook.setAuthor("Robert C. Martin");
        newBook.setIsbn("9780132350884");
        newBook.setPublishedYear(2008);

        String bookResponse = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        JsonNode bookJson = objectMapper.readTree(bookResponse);
        bookId = bookJson.get("id").asLong();
        assertThat(bookId).isNotNull();

        // ADD TWO COPIES
        String copyResponse1 = mockMvc.perform(post("/api/books/" + bookId + "/copies"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        copyId1 = objectMapper.readTree(copyResponse1).get("id").asLong();

        String copyResponse2 = mockMvc.perform(post("/api/books/" + bookId + "/copies"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        copyId2 = objectMapper.readTree(copyResponse2).get("id").asLong();

        // VERIFY BOOK BY ID
        mockMvc.perform(get("/api/books/" + bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.copies.length()").value(2));

        // UPDATE AVAILABILITY OF COPY 1
        BookCopyDtoUpdate updateCopy = BookCopyDtoUpdate.builder()
                .available(false)
                .build();

        mockMvc.perform(put("/api/books/" + bookId + "/copies/" + copyId1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCopy)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(copyId1))
                .andExpect(jsonPath("$.available").value(false));

        // DELETE BOOK
        mockMvc.perform(delete("/api/books/" + bookId))
                .andExpect(status().isNoContent());

        // VERIFY DELETION OF BOOK
        mockMvc.perform(get("/api/books/" + bookId))
                .andExpect(status().isNotFound());

        // VERIFY DELETION OF BOOK COPIES
        mockMvc.perform(get("/api/books/" + bookId + "/copies"))
                .andExpect(status().isNotFound());
    }
}
