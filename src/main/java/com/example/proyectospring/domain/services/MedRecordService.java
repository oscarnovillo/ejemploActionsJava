package com.example.proyectospring.domain.services;

import com.example.proyectospring.dao.model.Medication;
import com.example.proyectospring.domain.model.MedRecordUI;
import com.example.proyectospring.dao.model.MedRecord;
import com.example.proyectospring.dao.repositories.MedRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MedRecordService {
    private final MedRecordRepository medRecordRepository;

    public MedRecordService(MedRecordRepository medRecordRepository) {
        this.medRecordRepository = medRecordRepository;
    }

    public List<MedRecordUI> getAll(int idPatient)  {
        List<MedRecord> medRecords = medRecordRepository.getAll(idPatient);
        List<MedRecordUI> medRecordsUI= new ArrayList<>();
        medRecords.forEach(medRecord -> {
            // Crear una lista para almacenar los nombres de las medicaciones
            List<String> medicationNames = new ArrayList<>();
            // Iterar sobre las medicaciones en medRecord.getMedications() y extraer los nombres
            medRecord.getMedications().forEach(medication -> medicationNames.add(medication.getMedicationName()));

            // Construir MedRecordUI y establecer la lista de nombres de medicaciones
            medRecordsUI.add(MedRecordUI.builder()
                    .id(medRecord.getId())
                    .description(medRecord.getDiagnosis())
                    .date(String.valueOf(medRecord.getDate()))
                    .idPatient(medRecord.getIdPatient())
                    .idDoctor(medRecord.getIdDoctor())
                    .medications(medicationNames)
                    .build());
        });
        return medRecordsUI;

    }

    public void delete(int id) {medRecordRepository.delete(id);}

    public void update(MedRecordUI medRecordui) {
        LocalDate parsedDate = LocalDate.parse(medRecordui.getDate());
        //Transformo el array de String en array of medications
        List<Medication> medications = new ArrayList<>();
        for (String med : medRecordui.getMedications()) {
            medications.add(Medication.builder()
                    .medicationName(med)
                    .build());
        }
        medRecordRepository.update(MedRecord.builder()
                .id(medRecordui.getId())
                .diagnosis(medRecordui.getDescription())
                .date(parsedDate)
                .idPatient(medRecordui.getIdPatient())
                .idDoctor(medRecordui.getIdDoctor())
                .medications(medications)
                .build());
    }
    public int add(MedRecordUI medRecordui) {
        LocalDate parsedDate = LocalDate.parse(medRecordui.getDate());
        //Transformo el array de String en array of medications
        List<Medication> medications = new ArrayList<>();
        for (String med : medRecordui.getMedications()) {
            medications.add(Medication.builder()
                    .medicationName(med)
                    .build());
        }
        //Convierto medRecordUI en medRecord -- Hacer con builder mejor, en conversor en POJO, constructor
        int medRecordIndex = medRecordRepository.add(MedRecord.builder()
                .id(medRecordui.getId())
                .diagnosis(medRecordui.getDescription())
                .date(parsedDate)
                .idPatient(medRecordui.getIdPatient())
                .idDoctor(medRecordui.getIdDoctor())
                .medications(medications)
                .build());
        return medRecordIndex;
    }
}
