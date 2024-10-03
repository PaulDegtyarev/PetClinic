package ru.liga.petClinic.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.liga.petClinic.dto.PatientRequestBody;
import ru.liga.petClinic.dto.PatientResponseDto;
import ru.liga.petClinic.dto.StatusRequestBody;
import ru.liga.petClinic.service.PatientService;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        return new ResponseEntity<>(patientService.getAllPatients(), HttpStatus.OK);
    }

    @PostMapping("/base64")
    public ResponseEntity<PatientResponseDto> createPatientBase64(
            @RequestBody @Valid PatientRequestBody patientRequest,
            BindingResult bindingResult) {
        return new ResponseEntity<>(patientService.createPatientBase64(patientRequest, bindingResult), HttpStatus.CREATED);
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<PatientResponseDto> updatePatientStatusByPatientId(
            @PathVariable(required = false) Integer patientId,
            @RequestBody StatusRequestBody statusRequest
    ) {
        return new ResponseEntity<>(patientService.updatePatientStatusByPatientId(patientId, statusRequest), HttpStatus.OK);
    }

    @GetMapping("/{patientId}/image/base64")
    public ResponseEntity<byte[]> getPatientImage(@PathVariable Integer patientId) {
        byte[] imageBytes = patientService.getPatientImageBase64(patientId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }

    @PostMapping("/multipart")
    public ResponseEntity<PatientResponseDto> createPatient(
            @RequestParam("patientRequest") String patientRequestJson,
            @RequestParam("image") MultipartFile image) {
        ObjectMapper objectMapper = new ObjectMapper();
        PatientRequestBody patientRequest;
        try {
            patientRequest = objectMapper.readValue(patientRequestJson, PatientRequestBody.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(patientService.createPatientMultiPart(patientRequest, image), HttpStatus.CREATED);
    }

    @GetMapping("/{patientId}/image/multipart")
    public ResponseEntity<byte[]> getPatientImageMultipart(@PathVariable Integer patientId) {
        byte[] imageBytes = patientService.getPatientImageMultipart(patientId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }

    @PostMapping("/{patientId}/octet-stream")
    public ResponseEntity<String> uploadPatientImageOctetStream(
            @PathVariable Integer patientId,
            @RequestBody byte[] imageBytes) {
        patientService.uploadPatientImageOctetStream(patientId, imageBytes);
        return new ResponseEntity<>("Аватар пациента успешно добавлен", HttpStatus.CREATED);
    }

    @GetMapping("/{patientId}/image/octet-stream")
    public ResponseEntity<InputStreamResource> downloadPatientImageOctetStream(@PathVariable Integer patientId) {
        InputStreamResource resource = patientService.downloadPatientImageOctetStream(patientId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
