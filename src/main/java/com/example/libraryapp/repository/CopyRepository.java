package com.example.libraryapp.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CopyRepository {

    private final JdbcTemplate jdbcTemplate;

    public CopyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String,Object>> findAll() {
        return jdbcTemplate.queryForList("SELECT * FROM copies");
    }

    public void save(Long bookId, Long libraryId, String status) {
        jdbcTemplate.update(
            "INSERT INTO copies(book_id, library_id, status) VALUES (?, ?, ?)",
            bookId, libraryId, status
        );
    }
}