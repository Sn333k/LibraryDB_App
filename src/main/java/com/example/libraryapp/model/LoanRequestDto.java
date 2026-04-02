package com.example.libraryapp.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanRequestDto {

    @NotBlank
    private String loanDate;

    @NotBlank
    private String dueDate;

    @NotBlank
    private String returnDate;

    @NotBlank
    private String status;

    @NotNull
    private Long memberId;

    @NotNull
    private Long copyId;

    @NotNull
    private Long staffId;
}