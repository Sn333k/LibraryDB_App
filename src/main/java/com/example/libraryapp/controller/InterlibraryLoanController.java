package com.example.libraryapp.controller;

import com.example.libraryapp.model.InternalLoanRequestDto;
import com.example.libraryapp.repository.InterlibraryLoanRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/interlibrary-loans")
@CrossOrigin
public class InterlibraryLoanController {

    private final InterlibraryLoanRepository repository;

    public InterlibraryLoanController(InterlibraryLoanRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Map<String,Object>> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<String> add(@Valid @RequestBody InternalLoanRequestDto request) {
        repository.save(
                request.getLoanDate(),
                request.getReturnDate(),
                request.getCopyId(),
                request.getLendingLibraryId(),
                request.getTargetLibraryId()
        );

        return ResponseEntity.ok("OK");
    }
}