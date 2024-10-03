package ru.liga.petClinic.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequestBody {
    @NotBlank private String nickname;
    @NotBlank private String type;
    private String status;
    private String description;
    private String imageBase64;
}
