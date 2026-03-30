package com.example.libraryapp.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class AuthorRepository {
    private final JdbcTemplate jdbcTemplate;

    public AuthorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String,Object>> findAll() {
        return jdbcTemplate.queryForList("SELECT * FROM authors");
    }

    public void save(String firstName, String lastName, String nationality) {
        jdbcTemplate.update(
            "INSERT INTO authors(first_name, last_name, nationality) VALUES(?, ?, ?)",
            firstName, lastName, nationality
        );
    }
}