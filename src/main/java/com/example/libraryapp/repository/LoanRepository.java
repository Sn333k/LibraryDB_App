package com.example.libraryapp.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class LoanRepository {

    private final JdbcTemplate jdbcTemplate;

    public LoanRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String,Object>> findAll() {
        return jdbcTemplate.queryForList("SELECT * FROM loans");
    }

    public void save(String loanDate, String dueDate, String returnDate,
                     String status, Long memberId, Long copyId, Long staffId) {
        jdbcTemplate.update(
            "INSERT INTO loans(loan_date, due_date, return_date, status, member_id, copy_id, staff_id) VALUES (?, ?, ?, ?, ?, ?, ?)",
            loanDate, dueDate, returnDate, status, memberId, copyId, staffId
        );
    }
}