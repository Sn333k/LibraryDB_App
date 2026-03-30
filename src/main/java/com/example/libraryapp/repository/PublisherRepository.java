package com.example.libraryapp.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class PublisherRepository {
    private final JdbcTemplate jdbcTemplate;

    public PublisherRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String,Object>> findAll() {
        return jdbcTemplate.queryForList("SELECT * FROM publishers");
    }

    public void save(String name, String country, String email) {
        jdbcTemplate.update(
            "INSERT INTO publishers(publisher_name, country, contact_email) VALUES(?, ?, ?)",
            name, country, email
        );
    }
}