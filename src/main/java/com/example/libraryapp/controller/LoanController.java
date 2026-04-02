package com.example.libraryapp.controller;

import com.example.libraryapp.model.LoanRequestDto;
import com.example.libraryapp.repository.LoanRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/loans")
@CrossOrigin
public class LoanController {

    private final LoanRepository repository;

    public LoanController(LoanRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Map<String,Object>> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<String> add(@Valid @RequestBody LoanRequestDto request) {
        repository.save(
                request.getLoanDate(),
                request.getDueDate(),
                request.getReturnDate(),
                request.getStatus(),
                request.getMemberId(),
                request.getCopyId(),
                request.getStaffId()
        );

        return ResponseEntity.ok("OK");
    }
}