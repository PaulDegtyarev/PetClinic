package ru.liga.rest.repository.impl;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.liga.rest.dto.PatientRepositoryResponse;
import ru.liga.rest.entity.Patient;
import ru.liga.rest.repository.PatientsRepository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class DefaultPatientsRepository implements PatientsRepository {
    private static final Logger log = LogManager.getLogger(DefaultPatientsRepository.class);
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
}
