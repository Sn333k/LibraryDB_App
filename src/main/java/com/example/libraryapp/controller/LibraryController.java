package com.example.libraryapp.controller;

import com.example.libraryapp.model.LibraryRequestDto;
import com.example.libraryapp.repository.LibraryRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/libraries")
public class LibraryController {

    private final LibraryRepository libraryRepository;

    public LibraryController(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    @GetMapping
    public List<Map<String,Object>> getAll() {
        return libraryRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<String> addLibrary(@Valid @RequestBody LibraryRequestDto request) {
        libraryRepository.save(request.getCity(), request.getAddress());
        return ResponseEntity.ok("OK");
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @Valid @RequestBody LibraryRequestDto request) {
        libraryRepository.update(id, request.getCity(), request.getAddress());
        return ResponseEntity.ok("OK");
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        libraryRepository.delete(id);
    }
}