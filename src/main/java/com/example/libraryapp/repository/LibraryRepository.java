package com.example.libraryapp.repository;

import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@Repository
public class LibraryRepository {

    private final JdbcTemplate jdbcTemplate;

    public LibraryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> findAll() {
        return jdbcTemplate.queryForList("SELECT * FROM libraries");
    }

    public void save(String city, String address) {
        jdbcTemplate.update(
            "INSERT INTO libraries(city, address) VALUES(?, ?)",
            city, address
        );
    }
}