package ru.liga.petClinic.service;

import org.springframework.validation.BindingResult;
import ru.liga.petClinic.dto.PatientRequestBody;
import ru.liga.petClinic.dto.PatientResponseDto;
import ru.liga.petClinic.dto.StatusRequestBody;

import java.util.List;

public interface PatientService {
    List<PatientResponseDto> getAllPatients();


    PatientResponseDto createPatient(PatientRequestBody patientRequestBody, BindingResult bindingResult);

    PatientResponseDto updatePatientStatusByPatientId(Integer patientId, StatusRequestBody patientRequest);
}
