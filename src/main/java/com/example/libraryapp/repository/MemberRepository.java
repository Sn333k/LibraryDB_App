package com.example.libraryapp.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String,Object>> findAll() {
        return jdbcTemplate.queryForList("SELECT * FROM members");
    }

    public void save(String firstName, String lastName, String email,
                     String phone, String membershipDate, String status) {
        jdbcTemplate.update(
            "INSERT INTO members(first_name, last_name, email, phone, membership_date, membership_status) VALUES (?, ?, ?, ?, ?, ?)",
            firstName, lastName, email, phone, membershipDate, status
        );
    }
}