package com.example.proyectospring.dao.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Medication
{
    private int id;
    private String medicationName;
    private String dosage;
    private int medRecordId;
}

