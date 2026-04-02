package com.example.libraryapp.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationRequestDto {

    @NotNull
    private Long bookId;

    @NotNull
    private Long memberId;

    @NotBlank
    private String date;

    @NotBlank
    private String status;
}