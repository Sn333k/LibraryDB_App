package com.example.libraryapp.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBookRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String isbn;

    @NotNull
    private Integer year;

    @NotBlank
    private String genre;

    @NotNull
    private Long publisherId;
}