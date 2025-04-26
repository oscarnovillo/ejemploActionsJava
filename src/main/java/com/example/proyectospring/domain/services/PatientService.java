package com.example.proyectospring.domain.services;

import com.example.proyectospring.dao.model.Credential;
import com.example.proyectospring.dao.model.Patient;
import com.example.proyectospring.dao.model.Payment;
import com.example.proyectospring.dao.repositories.PatientRepository;

import com.example.proyectospring.dao.repositories.PaymentRepository;
import com.example.proyectospring.domain.model.PatientUI;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PaymentRepository paymentRepository;

    public PatientService(PatientRepository patientRepository, PaymentRepository paymentRepository) {
        this.patientRepository = patientRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<PatientUI> findAll()  {

        List<Patient> patients = patientRepository.getAll();

        //Get list of ids and call paymentRepository to get the payments
        List<Integer> idPatients= patients.stream()
                .map(Patient::getId) // Extrae el ID de cada paciente
                .collect(Collectors.toList());

        List<Payment> payments = paymentRepository.getAll(idPatients);
        //Build the List<PatientUI>
        Map<Integer, Payment> paymentMap = payments.stream()
                .collect(Collectors.toMap(Payment::getPatientId, payment -> payment));
        List<PatientUI> patientUIList = patients.stream()
                .map(patient -> {
                    Payment payment = paymentMap.get(patient.getId());
                    return new PatientUI(
                            patient.getId(),
                            patient.getName(),
                            patient.getBirthDate(),
                            patient.getPhone(),
                            payment != null ? (int) payment.getAmount() : 0,
                            patient.getCredential() != null ? patient.getCredential().getUsername() : null,
                            patient.getCredential() != null ? patient.getCredential().getPassword() : null
                    );
                })
                .collect(Collectors.toList());
        return patientUIList;
    }
    public Patient get(int id)  {
        return patientRepository.get(id);
    }

    public int add(PatientUI patientUi) {
        Credential credential= new Credential(patientUi.getUserName(), patientUi.getPassword(),0);
        //Es una transacción, se debe hacer en DAO. Enviar a add patient el paciente y el login
        return patientRepository.add(new Patient(0, patientUi.getName(), patientUi.getBirthDate(), patientUi.getPhone(),credential));

    }

    public  void update(PatientUI patientUi) {patientRepository.update(new Patient(patientUi.getId(), patientUi.getName(), patientUi.getBirthDate(), patientUi.getPhone()));}

    public void delete(int id, boolean confirmation) {patientRepository.delete(id, confirmation);}
}
