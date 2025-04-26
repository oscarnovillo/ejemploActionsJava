package com.example.proyectospring.dao.repositories;

import com.example.proyectospring.dao.mappers.MedRecordRowMapper;
import com.example.proyectospring.dao.model.MedRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Disabled("Prueba temporalmente deshabilitada")
@JdbcTest // Configura un entorno de prueba enfocado en JDBC
@Import({MedRecordRepository.class, MedRecordRowMapper.class, MedicationRepository.class}) // Importa las clases necesarias
@Testcontainers // Habilita la integración con Testcontainers
@ActiveProfiles("test") // Opcional: para perfiles de configuración específicos de test
class MedRecordRepositoryTest {

    // Define el contenedor MySQL
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.4"));

    @Autowired
    private MedRecordRepository medRecordRepository; // Repositorio a probar

    @Autowired
    private JdbcTemplate jdbcTemplate; // Para configurar la base de datos de prueba

    // Configura dinámicamente las propiedades de la fuente de datos para usar el contenedor
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.sql.init.mode", () -> "always"); // Asegura que se ejecuten scripts SQL si existen
    }

    // Método para configurar datos de prueba antes de cada test
    @BeforeEach
    void setUp() {
        // Limpiar tablas (opcional, depende de la estrategia)
        jdbcTemplate.update("DELETE FROM prescribed_medications");
        jdbcTemplate.update("DELETE FROM medical_records");
        jdbcTemplate.update("DELETE FROM patients"); // Asumiendo que existe y es necesaria por FK
        jdbcTemplate.update("DELETE FROM doctors");  // Asumiendo que existe y es necesaria por FK

        // Crear datos necesarios (ej. pacientes, doctores si hay FKs)
        jdbcTemplate.update("INSERT INTO patients (patient_id, name) VALUES (1, 'Test Patient')");
         // Asegúrate de que el paciente 2 exista si lo usas en las pruebas
        jdbcTemplate.update("INSERT INTO patients (patient_id, name) VALUES (2, 'Another Patient')");
        jdbcTemplate.update("INSERT INTO doctors (doctor_id, name) VALUES (10, 'Test Doctor')");

        // Insertar datos de prueba para medical_records
        jdbcTemplate.update("INSERT INTO medical_records (record_id, diagnosis, admission_date, patient_id, doctor_id) VALUES (?, ?, ?, ?, ?)",
                1, "Diagnosis 1", LocalDate.of(2024, 1, 15), 1, 10);
        jdbcTemplate.update("INSERT INTO medical_records (record_id, diagnosis, admission_date, patient_id, doctor_id) VALUES (?, ?, ?, ?, ?)",
                2, "Diagnosis 2", LocalDate.of(2024, 2, 20), 1, 10);
        jdbcTemplate.update("INSERT INTO medical_records (record_id, diagnosis, admission_date, patient_id, doctor_id) VALUES (?, ?, ?, ?, ?)",
                3, "Diagnosis 3", LocalDate.of(2024, 3, 25), 2, 10); // Otro paciente

        // Insertar datos de prueba para prescribed_medications
        jdbcTemplate.update("INSERT INTO prescribed_medications (medication_id, name, dosage, record_id) VALUES (?, ?, ?, ?)",
                101, "Med A", "10mg", 1);
        jdbcTemplate.update("INSERT INTO prescribed_medications (medication_id, name, dosage, record_id) VALUES (?, ?, ?, ?)",
                102, "Med B", "20mg", 1);
        jdbcTemplate.update("INSERT INTO prescribed_medications (medication_id, name, dosage, record_id) VALUES (?, ?, ?, ?)",
                103, "Med C", "5mg", 2);
    }

    @Test
    void getAll_shouldReturnRecordsWithMedicationsForPatient() {
        // Arrange
        int patientIdToFetch = 1;

        // Act
        List<MedRecord> actualMedRecords = medRecordRepository.getAll(patientIdToFetch);

        // Assert
        assertThat(actualMedRecords).isNotNull();
        assertThat(actualMedRecords).hasSize(2); // Esperamos 2 registros para el paciente 1

        // Verificar el primer registro médico y sus medicamentos
        MedRecord record1 = actualMedRecords.stream().filter(r -> r.getId() == 1).findFirst().orElse(null);
        assertThat(record1).isNotNull();
        assertThat(record1.getDiagnosis()).isEqualTo("Diagnosis 1");
        assertThat(record1.getDate()).isEqualTo(LocalDate.of(2024, 1, 15));
        assertThat(record1.getIdPatient()).isEqualTo(patientIdToFetch);
        assertThat(record1.getIdDoctor()).isEqualTo(10);
        assertThat(record1.getMedications()).hasSize(2);
        assertThat(record1.getMedications())
                .extracting("medicationName")
                .containsExactlyInAnyOrder("Med A", "Med B");
        assertThat(record1.getMedications())
                .extracting("dosage")
                .containsExactlyInAnyOrder("10mg", "20mg");


        // Verificar el segundo registro médico y sus medicamentos
        MedRecord record2 = actualMedRecords.stream().filter(r -> r.getId() == 2).findFirst().orElse(null);
        assertThat(record2).isNotNull();
        assertThat(record2.getDiagnosis()).isEqualTo("Diagnosis 2");
        assertThat(record2.getDate()).isEqualTo(LocalDate.of(2024, 2, 20));
        assertThat(record2.getIdPatient()).isEqualTo(patientIdToFetch);
        assertThat(record2.getIdDoctor()).isEqualTo(10);
        assertThat(record2.getMedications()).hasSize(1);
        assertThat(record2.getMedications().get(0).getMedicationName()).isEqualTo("Med C");
        assertThat(record2.getMedications().get(0).getDosage()).isEqualTo("5mg");

    }

     @Test
    void getAll_shouldReturnEmptyListForPatientWithNoRecords() {
        // Arrange
        int patientIdWithNoRecords = 99;
         jdbcTemplate.update("INSERT INTO patients (patient_id, name) VALUES (?, 'No Records Patient')", patientIdWithNoRecords);


        // Act
        List<MedRecord> actualMedRecords = medRecordRepository.getAll(patientIdWithNoRecords);

        // Assert
        assertThat(actualMedRecords).isNotNull();
        assertThat(actualMedRecords).isEmpty();
    }
}
