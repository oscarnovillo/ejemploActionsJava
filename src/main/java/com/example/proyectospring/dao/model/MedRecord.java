package com.example.proyectospring.dao.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedRecord {
        private int id;
        private int idPatient;
        private int idDoctor;
        private String diagnosis;
        private LocalDate date;
        private List<Medication> medications;
}
