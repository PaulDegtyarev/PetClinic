package ru.liga.petClinic.repository;

import ru.liga.petClinic.dto.PatientRepositoryResponse;
import ru.liga.petClinic.entity.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientsRepository {


    List<PatientRepositoryResponse> getAllPatients();

    PatientRepositoryResponse save(Patient patient);

    Optional<PatientRepositoryResponse> findPatientByPatientId(Integer patientId);

    void update(Integer patientId, Patient updatedPatient);

    boolean existsByNicknameAndType(String nickname, String type);
}
