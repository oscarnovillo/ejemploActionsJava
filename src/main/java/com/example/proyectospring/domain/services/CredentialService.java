package com.example.proyectospring.domain.services;

import com.example.proyectospring.dao.model.Credential;
import com.example.proyectospring.dao.model.Patient;
import com.example.proyectospring.dao.repositories.CredentialRepository;
import com.example.proyectospring.dao.repositories.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {
    private final CredentialRepository credentialRepository;

    public CredentialService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }
    public boolean isValid(Credential userCredential) {
        Credential daoCredential = credentialRepository.get(userCredential.getUsername());

        return daoCredential != null && daoCredential.getPassword().equals(userCredential.getPassword());
    }


}
