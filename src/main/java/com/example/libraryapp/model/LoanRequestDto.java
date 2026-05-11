package com.example.libraryapp.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LoanRequestDto {

    @NotNull
    private LocalDate loanDate;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    private LocalDate returnDate;

    @NotBlank
    private String status;

    @NotNull
    private Long memberId;

    @NotNull
    private Long bookId;

    @NotNull
    private Long library_id;

    @NotNull
    private Long staffId;
}