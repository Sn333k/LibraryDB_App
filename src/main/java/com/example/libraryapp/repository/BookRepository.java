package com.example.libraryapp.repository;

import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@Repository
public class BookRepository {
    private final JdbcTemplate jdbcTemplate;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String,Object>> findAll() {
        return jdbcTemplate.queryForList("SELECT * FROM books");
    }

    public void save(String title, String isbn, int year, String genre, Long publisherId) {
        jdbcTemplate.update(
            "INSERT INTO books(title, isbn, publication_year, genre, publisher_id) VALUES(?, ?, ?, ?, ?)",
            title, isbn, year, genre, publisherId
        );
        jdbcTemplate.queryForObject("SELECT currval(pg_get_serial_sequence('books','book_id'))", Long.class);
    }

    public void addAuthor(Long bookId, Long authorId) {
        jdbcTemplate.update(
            "INSERT INTO books_authors(book_id, author_id) VALUES(?, ?)",
            bookId, authorId
        );
    }

    public void update(Long id, String title, String isbn, int year, String genre, Long publisherId) {
        jdbcTemplate.update(
                "UPDATE books SET title=?, isbn=?, publication_year=?, genre=?, publisher_id=? WHERE book_id=?",
                title, isbn, year, genre, publisherId, id
        );
    }

    public void delete(Long id) {
        jdbcTemplate.update(
                "DELETE FROM books WHERE book_id=?",
                id
        );
    }
}