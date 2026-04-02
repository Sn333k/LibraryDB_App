package com.example.libraryapp.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookAuthorDto {

    @NotNull()
    @Positive()
    private Long bookId;

    @NotNull()
    @Positive()
    private Long authorId;
}