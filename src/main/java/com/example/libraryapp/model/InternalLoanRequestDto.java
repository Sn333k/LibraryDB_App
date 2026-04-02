package com.example.libraryapp.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalLoanRequestDto {

    @NotBlank
    private String loanDate;

    @NotBlank
    private String returnDate;

    @NotNull
    private Long copyId;

    @NotNull
    private Long lendingLibraryId;

    @NotNull
    private Long targetLibraryId;
}