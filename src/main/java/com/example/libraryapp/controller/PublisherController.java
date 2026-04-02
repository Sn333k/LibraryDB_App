package com.example.libraryapp.controller;

import com.example.libraryapp.model.PublisherRequestDto;
import com.example.libraryapp.repository.PublisherRepository;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> add(@Valid @RequestBody PublisherRequestDto request) {
        repository.save(
                request.getName(),
                request.getCountry(),
                request.getEmail()
        );

        return ResponseEntity.ok("OK");
    }
}