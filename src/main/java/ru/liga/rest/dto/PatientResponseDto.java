package ru.liga.rest.dto;

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
    private String type;
    private Timestamp date;
    private String status;
    private String description;
}
