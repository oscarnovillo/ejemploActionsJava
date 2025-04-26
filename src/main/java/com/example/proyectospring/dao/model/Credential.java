package com.example.proyectospring.dao.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Credential {
    private String username;
    private String password;
    private Integer patientId;
}
