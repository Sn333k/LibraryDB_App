package com.example.libraryapp.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class StaffRepository {

    private final JdbcTemplate jdbcTemplate;

    public StaffRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String,Object>> findAll() {
        return jdbcTemplate.queryForList("SELECT * FROM staff");
    }

    public void save(String firstName, String lastName, String role,
                     String email, String hireDate) {
        jdbcTemplate.update(
            "INSERT INTO staff(first_name, last_name, role, email, hire_date) VALUES (?, ?, ?, ?, ?)",
            firstName, lastName, role, email, hireDate
        );
    }
}