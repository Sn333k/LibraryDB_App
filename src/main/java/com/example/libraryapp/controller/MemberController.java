package com.example.libraryapp.controller;

import com.example.libraryapp.repository.MemberRepository;
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
    public void add(@RequestBody Map<String,String> body) {
        repository.save(
            body.get("firstName"),
            body.get("lastName"),
            body.get("email"),
            body.get("phone"),
            body.get("membershipDate"),
            body.get("status")
        );
    }
}