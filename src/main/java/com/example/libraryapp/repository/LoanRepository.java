package com.example.libraryapp.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class LoanRepository {

    private final JdbcTemplate jdbcTemplate;

    public LoanRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String,Object>> findAll() {
        return jdbcTemplate.queryForList("SELECT * FROM loans");
    }

    @Transactional
    public void save(LocalDate loanDate, LocalDate dueDate, LocalDate returnDate,
                     String status, Long memberId, Long bookId, Long staffId, Long libraryId) {

        Long availableCopyId;

        // 1. Znalezienie ID pierwszej dostępnej kopii (status 'A') dla danej książki w bibliotece
        try {
            availableCopyId = jdbcTemplate.queryForObject(
                    "SELECT copy_id FROM copies WHERE book_id = ? AND library_id = ? AND status = 'A' LIMIT 1 FOR UPDATE",
                    Long.class,
                    bookId, libraryId
            );

        } catch (EmptyResultDataAccessException e) {
            // Jeśli zapytanie nie zwróci żadnego wyniku, oznacza to brak dostępnych kopii
            throw new IllegalStateException("Brak dostępnych kopii książki o ID " + bookId + " w bibliotece " + libraryId + ".");
        }

        // 2. Zmiana statusu znalezionej kopii na status z requestu
        jdbcTemplate.update("UPDATE copies SET status = ? WHERE copy_id = ?", status, availableCopyId);

        // 3. Utworzenie wypożyczenia przypisanego do ZNALEZIONEJ kopii (zwróciłem brakujące library_id)
        jdbcTemplate.update(
                "INSERT INTO loans(loan_date, due_date, return_date, status, member_id, copy_id, staff_id) VALUES (?, ?, ?, ?, ?, ?, ?)",
                loanDate, dueDate, returnDate, status, memberId, availableCopyId, staffId
        );
    }
}