package com.example.proyectospring.domain.services;

import com.example.proyectospring.dao.model.Medication;
import com.example.proyectospring.dao.model.MedRecord;
import com.example.proyectospring.dao.repositories.MedRecordRepository;
import com.example.proyectospring.domain.model.MedRecordUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedRecordServiceTest {

    @Mock
    private MedRecordRepository medRecordRepository;

    @InjectMocks
    private MedRecordService medRecordService;

    private List<MedRecord> sampleMedRecords;
    private List<MedRecordUI> expectedMedRecordsUI;
    private int patientId;

    @BeforeEach
    void setUp() {
        patientId = 1;

        // Sample Medications
        Medication medication1 = Medication.builder().medicationName("Ibuprofen").build();
        Medication medication2 = Medication.builder().medicationName("Paracetamol").build();
        Medication medication3 = Medication.builder().medicationName("Aspirin").build();

        // Sample MedRecords from Repository
        MedRecord record1 = MedRecord.builder()
                .id(1)
                .diagnosis("Headache")
                .date(LocalDate.of(2025, 4, 26))
                .idPatient(patientId)
                .idDoctor(101)
                .medications(Arrays.asList(medication1, medication2))
                .build();
        MedRecord record2 = MedRecord.builder()
                .id(2)
                .diagnosis("Fever")
                .date(LocalDate.of(2025, 4, 27))
                .idPatient(patientId)
                .idDoctor(102)
                .medications(Collections.singletonList(medication3))
                .build();
        sampleMedRecords = Arrays.asList(record1, record2);

        // Expected MedRecordUI list
        MedRecordUI recordUI1 = MedRecordUI.builder()
                .id(1)
                .description("Headache")
                .date("2025-04-26")
                .idPatient(patientId)
                .idDoctor(101)
                .medications(Arrays.asList("Ibuprofen", "Paracetamol"))
                .build();
        MedRecordUI recordUI2 = MedRecordUI.builder()
                .id(2)
                .description("Fever")
                .date("2025-04-27")
                .idPatient(patientId)
                .idDoctor(102)
                .medications(Collections.singletonList("Aspirin"))
                .build();
        expectedMedRecordsUI = Arrays.asList(recordUI1, recordUI2);
    }

    @Test
    void getAll_shouldReturnMappedMedRecordsUI_whenRecordsExist() {
        // Arrange
        when(medRecordRepository.getAll(patientId)).thenReturn(sampleMedRecords);

        // Act
        List<MedRecordUI> actualMedRecordsUI = medRecordService.getAll(patientId);

        // Assert
        assertNotNull(actualMedRecordsUI);
        assertEquals(expectedMedRecordsUI.size(), actualMedRecordsUI.size());
        // Deep comparison of elements (consider overriding equals/hashCode in MedRecordUI or compare fields)
        for (int i = 0; i < expectedMedRecordsUI.size(); i++) {
            assertEquals(expectedMedRecordsUI.get(i).getId(), actualMedRecordsUI.get(i).getId());
            assertEquals(expectedMedRecordsUI.get(i).getDescription(), actualMedRecordsUI.get(i).getDescription());
            assertEquals(expectedMedRecordsUI.get(i).getDate(), actualMedRecordsUI.get(i).getDate());
            assertEquals(expectedMedRecordsUI.get(i).getIdPatient(), actualMedRecordsUI.get(i).getIdPatient());
            assertEquals(expectedMedRecordsUI.get(i).getIdDoctor(), actualMedRecordsUI.get(i).getIdDoctor());
            assertEquals(expectedMedRecordsUI.get(i).getMedications(), actualMedRecordsUI.get(i).getMedications());
        }
    }

    @Test
    void getAll_shouldReturnEmptyList_whenNoRecordsExist() {
        // Arrange
        when(medRecordRepository.getAll(patientId)).thenReturn(Collections.emptyList());

        // Act
        List<MedRecordUI> actualMedRecordsUI = medRecordService.getAll(patientId);

        // Assert
        assertNotNull(actualMedRecordsUI);
        assertTrue(actualMedRecordsUI.isEmpty());
    }
}
