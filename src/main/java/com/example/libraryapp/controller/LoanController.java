package com.example.libraryapp.controller;

import com.example.libraryapp.model.LoanRequestDto;
import com.example.libraryapp.repository.CopyRepository;
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

    private final LoanRepository loanRepository;
    private final CopyRepository copyRepository;

    public LoanController(LoanRepository loanRepository, CopyRepository copyRepository) {
        this.loanRepository = loanRepository;
        this.copyRepository = copyRepository;
    }

    @GetMapping
    public List<Map<String,Object>> getAll() {
        return loanRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<String> add(@Valid @RequestBody LoanRequestDto request) {

        Map<String, Object> copy =
                copyRepository.findOneAvailable(
                        request.getBookId(),
                        request.getCity()
                );

        if (copy == null) {
            return ResponseEntity.badRequest()
                    .body("Brak dostępnych egzemplarzy w tym mieście");
        }

        Long copyId = ((Number) copy.get("copy_id")).longValue();

        copyRepository.updateStatus(copyId, "L");

        loanRepository.save(
                request.getLoanDate(),
                request.getDueDate(),
                null,
                "ACTIVE",
                request.getMemberId(),
                copyId,
                request.getStaffId()
        );

        return ResponseEntity.ok("OK");
    }
}