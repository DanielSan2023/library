package com.example.library.controller;

import com.example.library.dto.bookdtos.BookDtoSimple;
import com.example.library.service.serviceimpl.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class LibraryController {

    private final BookService bookService;

    public LibraryController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/index")
    public String getHomePage() {
        return "index";
    }

    @GetMapping("/books-page")
    public String getBooksPage(@RequestParam(required = false) String name, Model model) {
        List<BookDtoSimple> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("name", name);
        return "books_page";
    }
}
