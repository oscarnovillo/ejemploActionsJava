package com.example.proyectospring.dao.mappers;

import com.example.proyectospring.dao.model.MedRecord;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component  //Hay que ponerlo para que Spring permita
public class MedRecordRowMapper implements RowMapper<MedRecord> {
    @Override
    public MedRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        MedRecord medRecord = new MedRecord();
        medRecord.setId(rs.getInt("record_id"));
        medRecord.setIdPatient(rs.getInt("patient_id"));
        medRecord.setIdDoctor(rs.getInt("doctor_id"));
        medRecord.setDiagnosis(rs.getString("diagnosis"));
        medRecord.setDate(rs.getDate("admission_date").toLocalDate());
        return medRecord;
    }
}
