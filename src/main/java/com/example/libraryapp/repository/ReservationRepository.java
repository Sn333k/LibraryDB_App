package com.example.libraryapp.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String,Object>> findAll() {
        return jdbcTemplate.queryForList("SELECT * FROM reservation");
    }

    public void save(Long bookId, Long memberId, String date, String status) {
        jdbcTemplate.update(
            "INSERT INTO reservation(book_id, member_id, reservation_date, status) VALUES (?, ?, ?, ?)",
            bookId, memberId, date, status
        );
    }
}