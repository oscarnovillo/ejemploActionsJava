package com.example.proyectospring.dao.repositories;

import com.example.proyectospring.dao.mappers.PatientRowMapper;
import com.example.proyectospring.dao.model.Patient;
import com.example.proyectospring.domain.errors.ForeignKeyConstraintError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class PatientRepository {

    private final JdbcClient jdbcClient;

    private final PatientRowMapper patientRowMapper;
    private final CredentialRepository credentialRepository;

    public PatientRepository(JdbcClient jdbcClient, PatientRowMapper patientRowMapper,CredentialRepository credentialRepository) {
        this.jdbcClient = jdbcClient;
        this.patientRowMapper = patientRowMapper;
        this.credentialRepository = credentialRepository;
    }

    public List<Patient> getAll() {
// Si todos los campos de la tabla coinciden con los campos de la clase, se puede usar la clase Patient.class directamente
//        List<Patient> lp= jdbcClient.sql("select * from patients")
//                .query(Patient.class).list();
// Como no es así, se debe hacer un mapeo manual
        List<Patient> lp= jdbcClient.sql("select * from patients")
                .query(patientRowMapper).list();
        return lp;
    }

    public Patient get(int id) {
        return jdbcClient.sql("select * from patients where id = ?")
                .param(1,id)
                .query(Patient.class).single();
    }

    @Transactional
    public int add(Patient patient) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("insert into patients(name, date_of_birth, phone) values (?,?,?)")
                .param(1, patient.getName())
                .param(2,patient.getBirthDate())
                .param(3,patient.getPhone())
                .update(keyHolder);

        int newId = Objects.requireNonNull(keyHolder.getKey(), "Key was not generated").intValue();
        patient.setId(newId);
        patient.getCredential().setPatientId(newId);
        credentialRepository.add(patient.getCredential());

        return newId;
    }

    public void update(Patient patient) {
        jdbcClient.sql("update patients set name = :name, date_of_birth = :dob, phone = :phone where patient_id = :id")
                .param("name", patient.getName())
                .param("dob", patient.getBirthDate())
                .param("phone", patient.getPhone())
                .param("id", patient.getId())
                .update();
    }

    public void delete(int id, boolean confirmation) {
        if (confirmation) {
            try {
                jdbcClient.sql("DELETE pm " +
                                "FROM prescribed_medications pm " +
                                "JOIN medical_records mr ON pm.record_id = mr.record_id " +
                                "WHERE mr.patient_id = ?")
                        .param(1, id)
                        .update();
                jdbcClient.sql("delete from medical_records where patient_id = ?")
                        .param(1, id)
                        .update();
                jdbcClient.sql("delete from appointments where patient_id = ?")
                        .param(1, id)
                        .update();
                jdbcClient.sql("delete from patient_payments where patient_id = ?")
                        .param(1, id)
                        .update();

            } catch (Exception e) {
                Logger.getLogger(PatientRepository.class.getName()).log(Level.SEVERE, "Error when deleting referential constraints", e);
            }
        }

        try {
            //Login deletion is not needed to be confirmed
            jdbcClient.sql("delete from user_login where patient_id = ?")
                    .param(1, id)
                    .update();
            int colAffected = jdbcClient.sql("delete from patients where patient_id = ?")
                    .param(1, id)
                    .update();
            // Operación de eliminación exitosa
        } catch (DataIntegrityViolationException e) {
            Logger.getLogger(PatientRepository.class.getName()).log(Level.SEVERE, "Patient with referential constraints", e);
            throw new ForeignKeyConstraintError("Patient with referential constraints");
        } catch (Exception e) {
            Logger.getLogger(PatientRepository.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}

