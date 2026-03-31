package com.example.libraryapp.controller;

import com.example.libraryapp.repository.InterlibraryLoanRepository;
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
    public void add(@RequestBody Map<String,Object> body) {
        repository.save(
            (String) body.get("loanDate"),
            (String) body.get("returnDate"),
            Long.valueOf(body.get("copyId").toString()),
            Long.valueOf(body.get("lendingLibraryId").toString()),
            Long.valueOf(body.get("targetLibraryId").toString())
        );
    }
}