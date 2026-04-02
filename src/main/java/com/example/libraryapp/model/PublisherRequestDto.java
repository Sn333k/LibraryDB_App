package com.example.libraryapp.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublisherRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String country;

    @NotBlank
    private String email;
}