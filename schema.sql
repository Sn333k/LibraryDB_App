CREATE EXTENSION IF NOT EXISTS pg_stat_statements;
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE LIBRARIES (
      library_id SERIAL PRIMARY KEY,
      city VARCHAR(20) NOT NULL,
      address VARCHAR(30) NOT NULL
);

CREATE TABLE PUBLISHERS (
      publisher_id SERIAL PRIMARY KEY,
      publisher_name VARCHAR(100) NOT NULL,
      country VARCHAR(50) NOT NULL,
      contact_email VARCHAR(100) NOT NULL
);

CREATE TABLE AUTHORS (
      author_id SERIAL PRIMARY KEY,
      first_name VARCHAR(100) NOT NULL,
      last_name VARCHAR(200) NOT NULL
);

CREATE TABLE BOOKS (
      book_id SERIAL PRIMARY KEY,
      title VARCHAR(200) NOT NULL,
      ISBN VARCHAR(20) NOT NULL UNIQUE,
      publication_year INT NOT NULL,
      genre VARCHAR(50) NOT NULL
);

CREATE TABLE BOOKS_AUTHORS (
      book_id INT NOT NULL,
      author_id INT NOT NULL,
      PRIMARY KEY (book_id, author_id),
      CONSTRAINT fk_ba_book FOREIGN KEY (book_id) REFERENCES BOOKS(book_id),
      CONSTRAINT fk_ba_author FOREIGN KEY (author_id) REFERENCES AUTHORS(author_id)
);

CREATE TABLE COPIES (
      copy_id SERIAL PRIMARY KEY,
      book_id INT NOT NULL,
      library_id INT NOT NULL,
      status VARCHAR(20) NOT NULL,
      CONSTRAINT fk_copies_book FOREIGN KEY (book_id) REFERENCES BOOKS(book_id),
      CONSTRAINT fk_copies_library FOREIGN KEY (library_id) REFERENCES LIBRARIES(library_id)
);

CREATE TABLE INTERLIBRARY_LOANS (
      il_loan_id SERIAL PRIMARY KEY,
      loan_date DATE,
      return_date DATE,
      copy_id INT,
      lending_library_id INT,
      target_library_id INT,
      CONSTRAINT fk_il_copy FOREIGN KEY (copy_id) REFERENCES COPIES(copy_id),
      CONSTRAINT fk_il_lending_library FOREIGN KEY (lending_library_id) REFERENCES LIBRARIES(library_id),
      CONSTRAINT fk_il_target_library FOREIGN KEY (target_library_id) REFERENCES LIBRARIES(library_id)
);

CREATE TABLE STAFF (
      staff_id SERIAL PRIMARY KEY,
      first_name VARCHAR(100) NOT NULL,
      last_name VARCHAR(100) NOT NULL,
      role VARCHAR(50) NOT NULL,
      email VARCHAR(100) NOT NULL,
      hire_date DATE NOT NULL
);

CREATE TABLE MEMBERS (
      member_id SERIAL PRIMARY KEY,
      first_name VARCHAR(100) NOT NULL,
      last_name VARCHAR(100) NOT NULL,
      email VARCHAR(100) NOT NULL,
      phone VARCHAR(15) NOT NULL,
      membership_date DATE NOT NULL,
      membership_status VARCHAR(20) NOT NULL
);

CREATE TABLE LOANS (
      loan_id SERIAL PRIMARY KEY,
      loan_date DATE NOT NULL,
      due_date DATE NOT NULL,
      return_date DATE,
      status VARCHAR(20) NOT NULL,
      member_id INT NOT NULL,
      copy_id INT NOT NULL,
      staff_id INT NOT NULL,
      CONSTRAINT fk_loans_member FOREIGN KEY (member_id) REFERENCES MEMBERS(member_id),
      CONSTRAINT fk_loans_copy FOREIGN KEY (copy_id) REFERENCES COPIES(copy_id),
      CONSTRAINT fk_loans_staff FOREIGN KEY (staff_id) REFERENCES STAFF(staff_id)
);

CREATE TABLE RESERVATION (
      reservation_id SERIAL PRIMARY KEY,
      book_id INT NOT NULL,
      member_id INT NOT NULL,
      reservation_date DATE NOT NULL,
      status VARCHAR(20) NOT NULL,
      CONSTRAINT fk_res_book FOREIGN KEY (book_id) REFERENCES BOOKS(book_id),
      CONSTRAINT fk_res_member FOREIGN KEY (member_id) REFERENCES MEMBERS(member_id)
);
--
----------------------------------------------------
-- ZOPTYMALIZOWANE INDEKSY I OGRANICZENIA
----------------------------------------------------

-- 1. Unikalność autorów odporna na wielkość liter (zastępuje zwykły ALTER TABLE)
CREATE UNIQUE INDEX unique_author_lower ON AUTHORS (LOWER(first_name), LOWER(last_name));

-- 2. Szybki indeks częściowy dostępnych egzemplarzy
CREATE INDEX idx_available_copies ON copies (book_id, library_id) WHERE status = 'A';

-- 3. Klucze obce do szybkich złączeń JOIN (Absolutny fundament wydajności)
CREATE INDEX idx_ba_book_id ON books_authors(book_id);
CREATE INDEX idx_ba_author_id ON books_authors(author_id);
CREATE INDEX idx_copies_book_id ON copies(book_id);
CREATE INDEX idx_copies_library_id ON copies(library_id);

-- 4. Indeksy GIN (Trigramy) dla Twojej wyszukiwarki ze Springa (ILIKE)
CREATE INDEX idx_books_title_trgm ON books USING GIN (title gin_trgm_ops);
CREATE INDEX idx_libraries_city_trgm ON libraries USING GIN (city gin_trgm_ops);
CREATE INDEX idx_authors_fullname_trgm ON authors USING GIN ((first_name || ' ' || last_name) gin_trgm_ops);