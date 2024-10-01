package ru.liga.rest.repository;

import ru.liga.rest.dto.PatientRepositoryResponse;
import ru.liga.rest.entity.Patient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PatientsRepository {


    List<PatientRepositoryResponse> getAllPatients();

    PatientRepositoryResponse save(Patient patient);

    Optional<PatientRepositoryResponse> findPatientByPatientId(Integer patientId);

    void update(Integer patientId, Patient updatedPatient);
}
