package com.example.libraryapp.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRequestDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String membershipDate;

    @NotBlank
    private String status;
}