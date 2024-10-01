package ru.liga.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.liga.rest.entity.Patient;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PatientRepositoryResponse {
    private Integer patientId;
    private Patient patientEntity;
}
