package com.example.libraryapp.controller;

import com.example.libraryapp.repository.BookRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
@CrossOrigin // pozwala frontendowi się połączyć
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping
    public void addBook(@RequestBody Map<String, Object> body) {
        bookRepository.save(
                (String) body.get("title"),
                (String) body.get("isbn"),
                (Integer) body.get("year"),
                (String) body.get("genre"),
                Long.valueOf(body.get("publisherId").toString())
        );
    }
    @GetMapping
    public List<Map<String, Object>> getAllBooks() {
        return bookRepository.findAll();
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Map<String,Object> body) {
        bookRepository.update(
                id,
                (String) body.get("title"),
                (String) body.get("isbn"),
                (Integer) body.get("year"),
                (String) body.get("genre"),
                Long.valueOf(body.get("publisherId").toString())
        );
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookRepository.delete(id);
    }
}