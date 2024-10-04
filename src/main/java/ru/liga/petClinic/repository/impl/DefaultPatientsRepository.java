package ru.liga.petClinic.repository.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.liga.petClinic.dto.PatientRepositoryResponse;
import ru.liga.petClinic.entity.Patient;
import ru.liga.petClinic.repository.PatientsRepository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@Log4j2
public class DefaultPatientsRepository implements PatientsRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DefaultPatientsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<PatientRepositoryResponse> getAllPatients() {
        return jdbcTemplate.queryForList("SELECT * FROM petclinic.patients")
                .stream()
                .map(row -> new PatientRepositoryResponse(
                        (Integer) row.get("id"),
                        new Patient(
                                (String) row.get("nickname"),
                                (String) row.get("pet"),
                                (Timestamp) row.get("appointmentdate"),
                                (String) row.get("status"),
                                (String) row.get("description")
                        )
                ))
                .toList();
    }

    @Override
    public PatientRepositoryResponse save(Patient patient) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO petclinic.patients (nickname, pet, appointmentdate, status, description) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, patient.getNickname());
            statement.setString(2, patient.getType());
            statement.setTimestamp(3, patient.getAppointmentdate());
            statement.setString(4, patient.getStatus());
            statement.setString(5, patient.getDescription());
            return statement;
        }, keyHolder);
        log.info("Сохраняю пациента с id: {}", keyHolder.getKeys());

        Map<String, Object> keys = keyHolder.getKeys();
        assert keys != null;
        Integer id = (Integer) keys.get("id");

        return new PatientRepositoryResponse(id, patient);
    }

    @Override
    public Optional<PatientRepositoryResponse> findPatientByPatientId(Integer patientId) {
        try {
            Patient patient = jdbcTemplate.queryForObject("SELECT * FROM petclinic.patients WHERE id = ?", new Object[]{patientId}, (resultSet, rowNum) -> new Patient(
                    resultSet.getString("nickname"),
                    resultSet.getString("pet"),
                    resultSet.getTimestamp("appointmentdate"),
                    resultSet.getString("status"),
                    resultSet.getString("description")
            ));
            return Optional.of(new PatientRepositoryResponse(patientId, patient));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(Integer patientId, Patient updatedPatient) {
        jdbcTemplate.update("UPDATE petclinic.patients SET nickname = ?, pet = ?, appointmentdate = ?, status = ?, description = ? WHERE id = ?",
                updatedPatient.getNickname(),
                updatedPatient.getType(),
                updatedPatient.getAppointmentdate(),
                updatedPatient.getStatus(),
                updatedPatient.getDescription(),
                patientId
        );
    }

    @Override
    public boolean existsByNicknameAndType(String nickname, String type) {
        return !jdbcTemplate.queryForList("SELECT * FROM petclinic.patients WHERE nickname = ? AND pet = ?", nickname, type).isEmpty();
    }
}
