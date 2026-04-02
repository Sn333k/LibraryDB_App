package com.example.libraryapp.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffRequestDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String role;

    @NotBlank
    private String email;

    @NotBlank
    private String hireDate;
}