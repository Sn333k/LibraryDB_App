package com.example.libraryapp.controller;

import com.example.libraryapp.model.StaffRequestDto;
import com.example.libraryapp.repository.StaffRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/staff")
@CrossOrigin
public class StaffController {

    private final StaffRepository repository;

    public StaffController(StaffRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Map<String,Object>> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<String> add(@Valid @RequestBody StaffRequestDto request) {
        repository.save(
                request.getFirstName(),
                request.getLastName(),
                request.getRole(),
                request.getEmail(),
                request.getHireDate()
        );

        return ResponseEntity.ok("OK");
    }
}