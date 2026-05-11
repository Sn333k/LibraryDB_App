package com.example.libraryapp.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationRequestDto {

    @NotNull
    private Long bookId;

    @NotNull
    private Long memberId;

    @NotNull
    private LocalDate date;

    @NotBlank
    private String status;
}