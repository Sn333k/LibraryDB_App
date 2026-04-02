package com.example.libraryapp.controller;

import com.example.libraryapp.model.AuthorRequestDto;
import com.example.libraryapp.repository.AuthorRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(repository.findAll());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Błąd zapytania: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> add(@Valid @RequestBody AuthorRequestDto request) {

        repository.save(
                request.getFirstName(),
                request.getLastName(),
                request.getNationality()
        );
        return ResponseEntity.ok("OK");
    }
}