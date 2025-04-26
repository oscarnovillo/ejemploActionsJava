package com.example.proyectospring.dao.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Payment {
 private int id;
 private int patientId;
 private double amount;
 private LocalDate paymentDate;
}
