package com.example.libraryapp.controller;

import com.example.libraryapp.repository.ReservationRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservations")
@CrossOrigin
public class ReservationController {

    private final ReservationRepository repository;

    public ReservationController(ReservationRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Map<String,Object>> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public void add(@RequestBody Map<String,Object> body) {
        repository.save(
            Long.valueOf(body.get("bookId").toString()),
            Long.valueOf(body.get("memberId").toString()),
            (String) body.get("date"),
            (String) body.get("status")
        );
    }
}