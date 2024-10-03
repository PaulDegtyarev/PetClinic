package ru.liga.petClinic.repository.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ru.liga.petClinic.dto.PatientRepositoryResponse;
import ru.liga.petClinic.entity.Patient;
import ru.liga.petClinic.repository.PatientsRepository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Log4j2
public class DefaultPatientsRepository implements PatientsRepository {
    private Map<Integer, Patient> patients = new HashMap<>();

    @PostConstruct
    private void fillOutPatientsMap() {
        patients.put(
                1, new Patient(
                        "Robert",
                        "Cat",
                        Timestamp.valueOf("2023-10-27 10:30:00"),
                        "HEALTHY",
                        "Годен"));
        patients.put(
                2, new Patient(
                        "Sharik",
                        "Dog",
                        Timestamp.valueOf("2023-10-27 10:30:00"),
                        "DEAD",
                        "Ресните Шарика, пж")
        );
    }

    @Override
    public List<PatientRepositoryResponse> getAllPatients() {
        return patients.entrySet()
                .stream()
                .map(integerPatientEntry -> new PatientRepositoryResponse(
                        integerPatientEntry.getKey(),
                        integerPatientEntry.getValue()))
                .toList();
    }

    @Override
    public PatientRepositoryResponse save(Patient patient) {
        Integer patientId = patients.size() + 1;
        log.info("Сохраняю пациента с id: {}", patientId);
        patients.put(patientId, patient);
        return new PatientRepositoryResponse(patientId, patient);
    }

    @Override
    public Optional<PatientRepositoryResponse> findPatientByPatientId(Integer patientId) {
        return Optional.ofNullable(patients.get(patientId))
                .map(patient -> new PatientRepositoryResponse(patientId, patient));
    }

    @Override
    public void update(Integer patientId, Patient updatedPatient) {
        patients.put(patientId, updatedPatient);
    }

    @Override
    public boolean existsByNicknameAndType(String nickname, String type) {
        return patients.values()
                .stream()
                .anyMatch(patient -> patient.getNickname().equalsIgnoreCase(nickname) && patient.getType().equalsIgnoreCase(type));
    }
}
