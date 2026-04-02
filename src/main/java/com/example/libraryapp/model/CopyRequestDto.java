package com.example.libraryapp.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CopyRequestDto {

    @NotNull
    private Long bookId;

    @NotNull
    private Long libraryId;

    @NotBlank
    private String status;
}