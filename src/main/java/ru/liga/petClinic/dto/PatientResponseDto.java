package ru.liga.petClinic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDto {
    private Integer id;
    private String nickname;
    private String pet;
    private Timestamp appointmentdate;
    private String status;
    private String description;
}
