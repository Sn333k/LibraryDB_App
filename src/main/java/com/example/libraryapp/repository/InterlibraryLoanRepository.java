package com.example.libraryapp.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class InterlibraryLoanRepository {

    private final JdbcTemplate jdbcTemplate;

    public InterlibraryLoanRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String,Object>> findAll() {
        return jdbcTemplate.queryForList("SELECT * FROM interlibrary_loans");
    }

    public void save(String loanDate, String returnDate, Long copyId,
                     Long lendingLibraryId, Long targetLibraryId) {
        jdbcTemplate.update(
            "INSERT INTO interlibrary_loans(loan_date, return_date, copy_id, lending_library_id, target_library_id) VALUES (?, ?, ?, ?, ?)",
            loanDate, returnDate, copyId, lendingLibraryId, targetLibraryId
        );
    }
}