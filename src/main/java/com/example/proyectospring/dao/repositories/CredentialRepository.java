package com.example.proyectospring.dao.repositories;

import com.example.proyectospring.dao.model.Credential;
import com.example.proyectospring.domain.errors.ForeignKeyConstraintError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class CredentialRepository {
    private final JdbcClient jdbcClient;
    public CredentialRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    public Credential get(String username) {

        List<Credential> credentials = jdbcClient.sql("select * from user_login where username = ?")
                .param(1, username)
                .query(Credential.class)
                .list();

        if (credentials.isEmpty()) {
            // Manejar el caso en que no se encontró el usuario
            return null; // o lanza una excepción, según lo que desees hacer
        } else {
            // Devolver el primer resultado
            return credentials.get(0);
        } // Utilizamos 'list()' en lugar de 'single()' por si el valor es nulo
    }

    public void add(Credential credential) {
        try {
            jdbcClient.sql("insert into user_login(username, password,patient_id ) values (?,?,?)")
                    .param(1, credential.getUsername())
                    .param(2, credential.getPassword())
                    .param(3, credential.getPatientId())
                    .update();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                Logger.getLogger(CredentialRepository.class.getName()).log(Level.SEVERE, "Username duplicated", e);
                throw new ForeignKeyConstraintError("Username duplicated");
            } else {
                Logger.getLogger(CredentialRepository.class.getName()).log(Level.SEVERE, "Unknown error", e);
                throw new ForeignKeyConstraintError("Unknown error");
            }

        } catch (Exception e) {
            Logger.getLogger(PatientRepository.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
