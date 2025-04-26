package com.example.proyectospring.dao.mappers;

import com.example.proyectospring.dao.model.Patient;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component  //Hay que ponerlo para que Spring permita
public class PatientRowMapper implements RowMapper<Patient> {
    @Override
    public Patient mapRow(ResultSet rs, int rowNum) throws SQLException {
        Patient patient = new Patient();
        patient.setId(rs.getInt("patient_id"));
        patient.setName(rs.getString("name"));
        patient.setBirthDate(rs.getDate("date_of_birth").toLocalDate());
        patient.setPhone(rs.getString("phone"));
        return patient;
    }
}
