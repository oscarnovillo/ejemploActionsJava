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
public class MedRecordRepository {

    private final JdbcClient jdbcClient;
    private final MedRecordRowMapper medRecordRowMapper;
    private final MedicationRepository medicationRepository;

    public MedRecordRepository(JdbcClient jdbcClient, MedRecordRowMapper medRecordRowMapper, MedicationRepository medicationRepository) {
        this.jdbcClient = jdbcClient;
        this.medRecordRowMapper = medRecordRowMapper;
        this.medicationRepository = medicationRepository;
    }

    public List<MedRecord> getAll(int idPatient) {
        List<MedRecord> medRecords= jdbcClient.sql("select * from medical_records where patient_id=?")
                .param(1,idPatient)
                .query(medRecordRowMapper).list();

        for (MedRecord medRecord : medRecords) {
            // TODO Esto tendría que ir en DAO prescribed_medications
            List<Medication> requirements =
                    jdbcClient.sql("select * from prescribed_medications where record_id=?")
                            .param(1,medRecord.getId())
                            .query(Medication.class).list();
            medRecord.setMedications(requirements);
        }
       return medRecords;
    }

    @Transactional
    public int add(MedRecord medRecord) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        //Add medical record
            jdbcClient.sql("insert into medical_records(diagnosis, admission_date, patient_id, doctor_id) values (?,?,?,?)")
                .param(1, medRecord.getDiagnosis())
                .param(2, medRecord.getDate())
                .param(3, medRecord.getIdPatient())
                .param(4, medRecord.getIdDoctor())
                .update(keyHolder);
        int newId = Objects.requireNonNull(keyHolder.getKey(), "Key was not generated").intValue();
        medRecord.setId(newId);
        //Add medications

        for (Medication medication : medRecord.getMedications()) {
           //En JDBC puro, se tiene que enviar la conexión para que sea transaccional, y poner autocommit a false
            // medicationDAO.add(connection, medication);
        //Con Spring, hace directamente con un @Transactional:
            //       medicalRecordDAO.add(record) y medicationDAO.add(medication) se ejecutan en la misma conexión de base de datos, proporcionada y gestionada por el PlatformTransactionManager de Spring.
            medication.setMedRecordId(newId);
            medicationRepository.add(medication);
                   }

        return newId;
    }

    public void delete(int id) {
        jdbcClient.sql("delete from prescribed_medications where record_id = ?")
                .param(1, id)
                .update();

        jdbcClient.sql("delete from medical_records where record_id = ?")
                .param(1, id)
                .update();
    }

    public void update(MedRecord app) {
        jdbcClient.sql("update medical_records set diagnosis = :desc, admission_date = :date, doctor_id = :idprof where record_id = :id")
                .param("desc", app.getDiagnosis())
                .param("date", app.getDate())
                .param("idprof", app.getIdDoctor())
                .param("id", app.getId())
                .update();

        //TODO update medications con @transactional
    }

}
