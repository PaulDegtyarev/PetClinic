package ru.liga.petClinic.service;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import ru.liga.petClinic.dto.PatientRequestBody;
import ru.liga.petClinic.dto.PatientResponseDto;
import ru.liga.petClinic.dto.StatusRequestBody;

import java.util.List;

public interface PatientService {
    List<PatientResponseDto> getAllPatients();

    PatientResponseDto createPatientBase64(PatientRequestBody patientRequestBody, BindingResult bindingResult);

    PatientResponseDto updatePatientStatusByPatientId(Integer patientId, StatusRequestBody patientRequest);

    byte[] getPatientImage(Integer patientId);

    PatientResponseDto createPatientMultiPart(PatientRequestBody patientRequestBody, MultipartFile image);

    void createPatientOctetStream(Integer patientId, byte[] imageBytes);
}
