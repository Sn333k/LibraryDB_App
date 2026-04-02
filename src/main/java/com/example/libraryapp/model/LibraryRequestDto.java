package com.example.libraryapp.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LibraryRequestDto {

    @NotBlank
    private String city;

    @NotBlank
    private String address;
}