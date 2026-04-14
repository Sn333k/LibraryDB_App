package com.example.libraryapp.controller;

import com.example.libraryapp.model.BookAuthorDto;
import com.example.libraryapp.repository.BooksAuthorsRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books-authors")
@CrossOrigin
public class BooksAuthorsController {

    private final BooksAuthorsRepository repository;

    public BooksAuthorsController(BooksAuthorsRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Map<String,Object>> getAll() {
        return repository.findAll();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchBooks(
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false, defaultValue = "") String author) {

        List<Map<String, Object>> results = repository.findByTitleAndAuthor(title, author);
        return ResponseEntity.ok(results);
    }


    @PostMapping
    public ResponseEntity<String> add(@Valid @RequestBody BookAuthorDto request) {
        repository.addRelation(request.getBookId(), request.getAuthorId());

        return ResponseEntity.ok("OK");
    }
}