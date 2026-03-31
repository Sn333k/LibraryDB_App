package com.example.libraryapp.controller;

import com.example.libraryapp.repository.StaffRepository;
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
    public void add(@RequestBody Map<String,String> body) {
        repository.save(
            body.get("firstName"),
            body.get("lastName"),
            body.get("role"),
            body.get("email"),
            body.get("hireDate")
        );
    }
}