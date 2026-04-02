package com.example.libraryapp.controller;

import com.example.libraryapp.model.ReservationRequestDto;
import com.example.libraryapp.repository.ReservationRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> add(@Valid @RequestBody ReservationRequestDto request) {
        repository.save(
                request.getBookId(),
                request.getMemberId(),
                request.getDate(),
                request.getStatus()
        );

        return ResponseEntity.ok("OK");
    }
}