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

    public void update(Long id, String city, String address) {
        jdbcTemplate.update(
                "UPDATE libraries SET city=?, address=? WHERE library_id=?",
                city, address, id
        );
    }

    public void delete(Long id) {
        jdbcTemplate.update(
                "DELETE FROM libraries WHERE library_id=?",
                id
        );
    }
}