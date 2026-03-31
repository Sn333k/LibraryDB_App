package com.example.libraryapp.controller;

import com.example.libraryapp.repository.LoanRepository;
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
    public void add(@RequestBody Map<String,Object> body) {
        repository.save(
            (String) body.get("loanDate"),
            (String) body.get("dueDate"),
            (String) body.get("returnDate"),
            (String) body.get("status"),
            Long.valueOf(body.get("memberId").toString()),
            Long.valueOf(body.get("copyId").toString()),
            Long.valueOf(body.get("staffId").toString())
        );
    }
}