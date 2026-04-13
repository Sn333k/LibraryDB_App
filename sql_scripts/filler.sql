CREATE TABLE temp_books_authors (
        book_isbn VARCHAR(20),
        author_fullname TEXT
);

COPY AUTHORS(first_name, last_name)
    FROM 'D:/PWR/SM SEM 1/Zaawansowane BAZY DANYCH/LibraryApp/csv_output/authors.csv'
    WITH (FORMAT CSV, HEADER, QUOTE '|', DELIMITER ',');

COPY BOOKS(title, ISBN, publication_year, genre)
    FROM 'D:/PWR/SM SEM 1/Zaawansowane BAZY DANYCH/LibraryApp/csv_output/books.csv'
    WITH ( FORMAT CSV, HEADER, QUOTE E'\b', DELIMITER ',');

COPY temp_books_authors(book_isbn, author_fullname)
    FROM 'D:/PWR/SM SEM 1/Zaawansowane BAZY DANYCH/LibraryApp/csv_output/books_authors.csv'
    WITH (FORMAT CSV, HEADER, QUOTE '|', DELIMITER ',');

INSERT INTO BOOKS_AUTHORS (book_id, author_id)
SELECT DISTINCT b.book_id, a.author_id
FROM temp_books_authors tba
         JOIN BOOKS b ON tba.book_isbn = b.ISBN
         JOIN AUTHORS a ON (a.first_name || ' ' || a.last_name) = tba.author_fullname
    OR a.last_name = tba.author_fullname
ON CONFLICT (book_id, author_id) DO NOTHING;

DROP TABLE temp_books_authors;