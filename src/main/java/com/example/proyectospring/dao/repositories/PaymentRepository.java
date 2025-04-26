package com.example.proyectospring.dao.repositories;

import com.example.proyectospring.dao.mappers.PatientRowMapper;
import com.example.proyectospring.dao.model.Patient;
import com.example.proyectospring.dao.model.Payment;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentRepository {
    private final JdbcClient jdbcClient;

    public PaymentRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Payment> getAll(List<Integer> idPatients) {
        StringBuilder sb = new StringBuilder("select patient_id,sum(amount) as amount from patient_payments where patient_id in (");
        for (int i = 0; i < idPatients.size(); i++) {
            sb.append(idPatients.get(i));
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);  // remove the last comma
        sb.append(") group by patient_id");

        String sql = sb.toString();

        List<Payment> lp= jdbcClient.sql(sql)
                .query(Payment.class).list();
        return lp;
    }
}
