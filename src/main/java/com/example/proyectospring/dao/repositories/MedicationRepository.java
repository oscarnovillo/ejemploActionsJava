package com.example.proyectospring.dao.repositories;

import com.example.proyectospring.dao.mappers.MedRecordRowMapper;
import com.example.proyectospring.dao.model.MedRecord;
import com.example.proyectospring.dao.model.Medication;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Repository
public class MedicationRepository {
    private final JdbcClient jdbcClient;

    public MedicationRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void add(Medication medication) {
        jdbcClient.sql("insert into prescribed_medications(record_id, medication_name, dosage) values (?,?,?)")
                .param(1, medication.getMedRecordId())
                .param(2, medication.getMedicationName())
                .param(3, medication.getDosage())
                .update();
      }
}
