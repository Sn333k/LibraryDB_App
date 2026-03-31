package com.example.libraryapp.controller;

import com.example.libraryapp.repository.AuthorRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/authors")
@CrossOrigin
public class AuthorController {

    private final AuthorRepository repository;

    public AuthorController(AuthorRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Map<String,Object>> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public void add(@RequestBody Map<String,String> body) {
        repository.save(
            body.get("firstName"),
            body.get("lastName"),
            body.get("nationality")
        );
    }
}