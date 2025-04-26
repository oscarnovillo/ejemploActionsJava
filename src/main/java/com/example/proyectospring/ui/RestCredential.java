package com.example.proyectospring.ui;

import com.example.proyectospring.dao.model.Credential;
import com.example.proyectospring.domain.services.CredentialService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class RestCredential {

    private final CredentialService credentialService;

    public RestCredential(CredentialService credentialService) {
        this.credentialService = credentialService;
    }


    @PostMapping("/login")
    public boolean login(@RequestBody Credential userCredentials) {

        return credentialService.isValid(userCredentials);
        //return userCredentials.getUsername() != null && !userCredentials.getUsername().isEmpty()
        //        && userCredentials.getPassword() != null && !userCredentials.getPassword().isEmpty();
    }
}
