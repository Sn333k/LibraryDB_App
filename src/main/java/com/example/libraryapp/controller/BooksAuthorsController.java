package com.example.libraryapp.controller;

import com.example.libraryapp.repository.BooksAuthorsRepository;
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

    @PostMapping
    public void add(@RequestBody Map<String,Long> body) {
        repository.addRelation(body.get("bookId"), body.get("authorId"));
    }
}