package com.example.libraryapp.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorRequestDto {

    @NotBlank()
    private String firstName;

    @NotBlank()
    private String lastName;

    @NotBlank()
    private String nationality;
}