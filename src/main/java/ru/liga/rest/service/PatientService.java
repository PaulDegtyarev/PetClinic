package ru.liga.rest.service;

import ru.liga.rest.dto.PatientRequestBody;
import ru.liga.rest.dto.PatientResponseDto;
import ru.liga.rest.dto.StatusRequestBody;

import java.util.List;

public interface PatientService {
    List<PatientResponseDto> getAllPatients();

    PatientResponseDto createPatient(PatientRequestBody patientRequestBody);

    PatientResponseDto updatePatientStatusByPatientId(Integer patientId, StatusRequestBody patientRequest);
}
