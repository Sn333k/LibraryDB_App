package com.example.libraryapp.controller;

import com.example.libraryapp.model.BookRequestDto;
import com.example.libraryapp.model.UpdateBookRequestDto;
import com.example.libraryapp.repository.AuthorRepository;
import com.example.libraryapp.repository.BookRepository;
import com.example.libraryapp.repository.BooksAuthorsRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
@CrossOrigin
public class BookController {

    private final BookRepository bookRepository;
    private final BooksAuthorsRepository booksAuthorsRepository;
    private final AuthorRepository authorRepository;


    public BookController(BookRepository bookRepository,
                          BooksAuthorsRepository booksAuthorsRepository,
                          AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.booksAuthorsRepository = booksAuthorsRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<String> addBook(@Valid @RequestBody BookRequestDto request) {

        Long bookId = bookRepository.save(
                request.getTitle(),
                request.getIsbn(),
                request.getYear(),
                request.getGenre(),
                request.getPublisherId()
        );

        String firstName = request.getAuthorFirstName(); //Author data
        String lastName = request.getAuthorLastName();
        String nationality = request.getNationality();

        Long authorId = authorRepository.findOrCreate(firstName, lastName, nationality); //Find or add author

        booksAuthorsRepository.addRelation(bookId, authorId);// Add relations author-book
        return ResponseEntity.ok("OK");
    }

    @GetMapping
    public List<Map<String, Object>> getAllBooks() {
        return bookRepository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @Valid @RequestBody UpdateBookRequestDto request) {
        bookRepository.update(
                id,
                request.getTitle(),
                request.getIsbn(),
                request.getYear(),
                request.getGenre(),
                request.getPublisherId()
        );
        return ResponseEntity.ok("OK");
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookRepository.delete(id);
    }
}