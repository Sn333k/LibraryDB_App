package com.example.libraryapp.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BooksAuthorsRepository {

    private final JdbcTemplate jdbcTemplate;

    public BooksAuthorsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addRelation(Long bookId, Long authorId) {
        jdbcTemplate.update(
            "INSERT INTO books_authors(book_id, author_id) VALUES (?, ?)",
            bookId, authorId
        );
    }

    public List<Map<String,Object>> findAll() {
        return jdbcTemplate.queryForList("SELECT * FROM books_authors");
    }
}