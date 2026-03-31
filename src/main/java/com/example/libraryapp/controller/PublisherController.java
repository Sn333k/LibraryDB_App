package com.example.libraryapp.controller;

import com.example.libraryapp.repository.PublisherRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/publishers")
@CrossOrigin
public class PublisherController {

    private final PublisherRepository repository;

    public PublisherController(PublisherRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Map<String,Object>> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public void add(@RequestBody Map<String,String> body) {
        repository.save(
            body.get("name"),
            body.get("country"),
            body.get("email")
        );
    }
}