package com.example.libraryapp.controller;

import com.example.libraryapp.repository.LibraryRepository;
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
    public void addLibrary(@RequestBody Map<String,String> body) {
        libraryRepository.save(body.get("city"), body.get("address"));
    }
}