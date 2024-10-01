package ru.liga.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.liga.rest.dto.PatientRequestBody;
import ru.liga.rest.dto.PatientResponseDto;
import ru.liga.rest.dto.StatusRequestBody;
import ru.liga.rest.service.PatientService;

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
    public ResponseEntity<PatientResponseDto> createPatient(@RequestBody PatientRequestBody patientRequest) {
        return new ResponseEntity<>(patientService.createPatient(patientRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<PatientResponseDto> updatePatientStatusByPatientId(
            @PathVariable(required = false) Integer patientId,
            @RequestBody StatusRequestBody statusRequest
    ) {
        return new ResponseEntity<>(patientService.updatePatientStatusByPatientId(patientId, statusRequest), HttpStatus.OK);
    }
}
