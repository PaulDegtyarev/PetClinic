package ru.liga.petClinic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.liga.petClinic.dto.PatientRepositoryResponse;
import ru.liga.petClinic.dto.PatientRequestBody;
import ru.liga.petClinic.dto.PatientResponseDto;
import ru.liga.petClinic.dto.StatusRequestBody;
import ru.liga.petClinic.entity.Patient;
import ru.liga.petClinic.exception.PatientBadRequestException;
import ru.liga.petClinic.exception.PatientConflictException;
import ru.liga.petClinic.exception.PatientNotFoundException;
import ru.liga.petClinic.repository.PatientsRepository;
import ru.liga.petClinic.service.PatientService;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultPatientService implements PatientService {
    private PatientsRepository patientsRepository;

    @Autowired
    public DefaultPatientService(PatientsRepository patientsRepository) {
        this.patientsRepository = patientsRepository;
    }

    @Override
    public List<PatientResponseDto> getAllPatients() {
        List<PatientRepositoryResponse> patients = patientsRepository.getAllPatients();

        return patients.stream()
                .map(patientRepositoryResponse -> new PatientResponseDto(
                        patientRepositoryResponse.getPatientId(),
                        patientRepositoryResponse.getPatientEntity().getNickname(),
                        patientRepositoryResponse.getPatientEntity().getType(),
                        patientRepositoryResponse.getPatientEntity().getDate(),
                        patientRepositoryResponse.getPatientEntity().getStatus(),
                        patientRepositoryResponse.getPatientEntity().getDescription()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public PatientResponseDto createPatient(PatientRequestBody patientRequestBody, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new PatientBadRequestException("nickname и type пациента должны быть обязательно заполнены");
        }

        if (patientsRepository.existsByNicknameAndType(
                patientRequestBody.getNickname(),
                patientRequestBody.getType()
        )) {
            throw new PatientConflictException("nickname " + patientRequestBody.getNickname() + " и type " + patientRequestBody.getType() + " заняты");
        }

        Patient newPatient = new Patient(
                patientRequestBody.getNickname(),
                patientRequestBody.getType(),
                new Timestamp(System.currentTimeMillis()),
                patientRequestBody.getStatus(),
                patientRequestBody.getDescription()
        );

        PatientRepositoryResponse savedPatient = patientsRepository.save(newPatient);

        return new PatientResponseDto(
                savedPatient.getPatientId(),
                savedPatient.getPatientEntity().getNickname(),
                savedPatient.getPatientEntity().getType(),
                savedPatient.getPatientEntity().getDate(),
                savedPatient.getPatientEntity().getStatus(),
                savedPatient.getPatientEntity().getDescription()
        );
    }

    @Override
    public PatientResponseDto updatePatientStatusByPatientId(Integer patientId, StatusRequestBody statusRequest) {
        PatientRepositoryResponse repositoryResponse = patientsRepository.findPatientByPatientId(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Пациент с номером: " + patientId + " не существует"));

        Patient foundPatientToUpdate = repositoryResponse.getPatientEntity();
        foundPatientToUpdate.updateStatus(statusRequest.getStatus());
        patientsRepository.update(patientId, foundPatientToUpdate);

        return new PatientResponseDto(
                patientId,
                foundPatientToUpdate.getNickname(),
                foundPatientToUpdate.getType(),
                foundPatientToUpdate.getDate(),
                foundPatientToUpdate.getStatus(),
                foundPatientToUpdate.getDescription()
        );
    }

}
