package com.example.libraryapp.controller;

import com.example.libraryapp.model.MemberRequestDto;
import com.example.libraryapp.repository.MemberRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/members")
@CrossOrigin
public class MemberController {

    private final MemberRepository repository;

    public MemberController(MemberRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Map<String,Object>> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<String> add(@Valid @RequestBody MemberRequestDto request) {
        repository.save(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone(),
                request.getMembershipDate(),
                request.getStatus()
        );

        return ResponseEntity.ok("OK");
    }
}