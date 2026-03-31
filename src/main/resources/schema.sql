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
      last_name VARCHAR(100) NOT NULL,
      nationality VARCHAR(50) NOT NULL
);

CREATE TABLE BOOKS (
      book_id SERIAL PRIMARY KEY,
      title VARCHAR(200) NOT NULL,
      ISBN VARCHAR(20) NOT NULL,
      publication_year INT NOT NULL,
      publisher_id INT NOT NULL,
      genre VARCHAR(50) NOT NULL,
      CONSTRAINT fk_books_publisher FOREIGN KEY (publisher_id)
      REFERENCES PUBLISHERS(publisher_id)
);

CREATE TABLE BOOKS_AUTHORS (
      book_id SERIAL NOT NULL,
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