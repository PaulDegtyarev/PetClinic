package ru.liga.petClinic.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(
            @RequestBody @Valid PatientRequestBody patientRequest,
            BindingResult bindingResult) {
        return new ResponseEntity<>(patientService.createPatient(patientRequest, bindingResult), HttpStatus.CREATED);
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<PatientResponseDto> updatePatientStatusByPatientId(
            @PathVariable(required = false) Integer patientId,
            @RequestBody StatusRequestBody statusRequest
    ) {
        return new ResponseEntity<>(patientService.updatePatientStatusByPatientId(patientId, statusRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getPatientImage(@PathVariable Integer id) {
        byte[] imageBytes = patientService.getPatientImage(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }
}
