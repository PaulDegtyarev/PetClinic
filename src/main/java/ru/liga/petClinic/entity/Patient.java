package ru.liga.petClinic.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private String nickname;
    private String type;
    private Timestamp date;
    private String status;
    private String description;

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }
}
