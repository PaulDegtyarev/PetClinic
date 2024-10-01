package ru.liga.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequestBody {
    private String nickname;
    private String type;
    private String status;
    private String description;
}
