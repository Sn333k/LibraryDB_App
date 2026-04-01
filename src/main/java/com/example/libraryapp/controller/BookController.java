package com.example.libraryapp.controller;

import com.example.libraryapp.repository.AuthorRepository;
import com.example.libraryapp.repository.BookRepository;
import com.example.libraryapp.repository.BooksAuthorsRepository;
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
    public void addBook(@RequestBody Map<String, Object> body) {

        Long bookId = bookRepository.save(
                (String) body.get("title"),
                (String) body.get("isbn"),
                (Integer) body.get("year"),
                (String) body.get("genre"),
                Long.valueOf(body.get("publisherId").toString())
        );

        String firstName = (String) body.get("authorFirstName"); //Author data
        String lastName = (String) body.get("authorLastName");
        String nationality = (String) body.get("nationality");

        Long authorId = authorRepository.findOrCreate(firstName, lastName, nationality); //Find or add author

        booksAuthorsRepository.addRelation(bookId, authorId);// Add relations author-book
    }

    @GetMapping
    public List<Map<String, Object>> getAllBooks() {
        return bookRepository.findAll();
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Map<String,Object> body) {
        bookRepository.update(
                id,
                (String) body.get("title"),
                (String) body.get("isbn"),
                (Integer) body.get("year"),
                (String) body.get("genre"),
                Long.valueOf(body.get("publisherId").toString())
        );
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookRepository.delete(id);
    }
}