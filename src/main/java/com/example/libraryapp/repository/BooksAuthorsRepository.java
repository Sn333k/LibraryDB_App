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

    public List<Map<String, Object>> findByTitleAndAuthor(String title, String authorName, String library) {
        String sql = """
        SELECT 
            b.book_id, 
            b.title, 
            b.isbn, 
            b.publication_year, 
            STRING_AGG(DISTINCT a.first_name || ' ' || a.last_name, ', ') AS authors,
            COUNT(DISTINCT c.copy_id) AS total_copies
        FROM books b
        JOIN books_authors ba ON b.book_id = ba.book_id
        JOIN authors a ON ba.author_id = a.author_id
        LEFT JOIN copies c ON b.book_id = c.book_id
        JOIN libraries l ON c.library_id = l.library_id
        WHERE b.title ILIKE ? 
          AND (a.first_name || ' ' || a.last_name) ILIKE ?
          AND l.city ILIKE ?
        GROUP BY b.book_id, b.title, b.isbn, b.publication_year
    """;

        String searchTitle = "%" + title + "%";
        String searchAuthor = "%" + authorName + "%";
        String searchLibrary = "%" + library + "%";

        return jdbcTemplate.queryForList(sql, searchTitle, searchAuthor,searchLibrary);
    }
}