package ru.liga.petClinic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import ru.liga.petClinic.dto.PatientRepositoryResponse;
import ru.liga.petClinic.dto.PatientRequestBody;
import ru.liga.petClinic.dto.PatientResponseDto;
import ru.liga.petClinic.dto.StatusRequestBody;
import ru.liga.petClinic.entity.Patient;
import ru.liga.petClinic.exception.FileNotFoundException;
import ru.liga.petClinic.exception.PatientBadRequestException;
import ru.liga.petClinic.exception.PatientConflictException;
import ru.liga.petClinic.exception.PatientNotFoundException;
import ru.liga.petClinic.repository.PatientsRepository;
import ru.liga.petClinic.service.PatientService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
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
    public PatientResponseDto createPatientBase64(PatientRequestBody patientRequestBody, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new PatientBadRequestException("nickname и type пациента должны быть обязательно заполнены");
        }

        if (patientsRepository.existsByNicknameAndType(
                patientRequestBody.getNickname(),
                patientRequestBody.getType()
        )) {
            throw new PatientConflictException("nickname " + patientRequestBody.getNickname() + " и type " + patientRequestBody.getType() + " заняты");
        }

        byte[] imageBytes = Base64.getDecoder().decode(patientRequestBody.getImageBase64());
        String directoryPath = "images";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }


        Patient newPatient = new Patient(
                patientRequestBody.getNickname(),
                patientRequestBody.getType(),
                new Timestamp(System.currentTimeMillis()),
                patientRequestBody.getStatus(),
                patientRequestBody.getDescription()
        );

        PatientRepositoryResponse savedPatient = patientsRepository.save(newPatient);

        String imagePath = directoryPath + File.separator + savedPatient.getPatientId() + ".png";
        try (FileOutputStream fos = new FileOutputStream(imagePath)) {
            fos.write(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении изображения: " + e.getMessage(), e);
        }

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

    @Override
    public byte[] getPatientImageBase64(Integer patientId) {
        String directoryPath = "images";
        String imagePath = directoryPath + File.separator + patientId + ".png";

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new FileNotFoundException("Изображение с ID " + patientId + " не найдено");
        }

        try (FileInputStream fis = new FileInputStream(imageFile)) {
            byte[] imageBytes = new byte[(int) imageFile.length()];
            fis.read(imageBytes);

            return imageBytes;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении изображения: " + e.getMessage(), e);
        }
    }

    @Override
    public PatientResponseDto createPatientMultiPart(PatientRequestBody patientRequestBody, MultipartFile image) {
        if (patientsRepository.existsByNicknameAndType(
                patientRequestBody.getNickname(),
                patientRequestBody.getType()
        )) {
            throw new PatientConflictException("nickname " + patientRequestBody.getNickname() + " и type " + patientRequestBody.getType() + " заняты");
        }

        String directoryPath = "images";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            byte[] imageBytes = image.getBytes();

            Patient newPatient = new Patient(
                    patientRequestBody.getNickname(),
                    patientRequestBody.getType(),
                    new Timestamp(System.currentTimeMillis()),
                    patientRequestBody.getStatus(),
                    patientRequestBody.getDescription()
            );

            PatientRepositoryResponse savedPatient = patientsRepository.save(newPatient);

            String imagePath = directoryPath + File.separator + savedPatient.getPatientId() + ".png";
            try (FileOutputStream fos = new FileOutputStream(imagePath)) {
                fos.write(imageBytes);
            }

            return new PatientResponseDto(
                    savedPatient.getPatientId(),
                    savedPatient.getPatientEntity().getNickname(),
                    savedPatient.getPatientEntity().getType(),
                    savedPatient.getPatientEntity().getDate(),
                    savedPatient.getPatientEntity().getStatus(),
                    savedPatient.getPatientEntity().getDescription()
            );
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении изображения: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] getPatientImageMultipart(Integer patientId) {
        String directoryPath = "images";
        String imagePath = directoryPath + File.separator + patientId + ".png";

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new FileNotFoundException("Изображение с ID " + patientId + " не найдено");
        }

        try (FileInputStream fis = new FileInputStream(imageFile)) {
            byte[] imageBytes = new byte[(int) imageFile.length()];
            fis.read(imageBytes);

            return imageBytes;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении изображения: " + e.getMessage(), e);
        }
    }
}
