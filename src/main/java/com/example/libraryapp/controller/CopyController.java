package com.example.libraryapp.controller;

import com.example.libraryapp.model.CopyRequestDto;
import com.example.libraryapp.repository.CopyRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/copies")
@CrossOrigin
public class CopyController {

    private final CopyRepository repository;

    public CopyController(CopyRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Map<String,Object>> getAll() {
        return repository.findAll();
    }

    @GetMapping("/search-available")
    public ResponseEntity<List<Map<String, Object>>> searchAvailable(
            @RequestParam Long bookId,
            @RequestParam(required = false, defaultValue = "") String city) {

        String searchCity = "%" + city + "%";

        List<Map<String, Object>> results = repository.findAvailableByBookAndCity(bookId, searchCity);
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<String> add(@Valid @RequestBody CopyRequestDto request) {
        repository.save(
                request.getBookId(),
                request.getLibraryId(),
                request.getStatus()
        );

        return ResponseEntity.ok("OK");
    }
}