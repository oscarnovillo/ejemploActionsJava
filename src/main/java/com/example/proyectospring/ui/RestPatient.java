package com.example.proyectospring.ui;


import com.example.proyectospring.dao.model.Patient;
import com.example.proyectospring.domain.services.PatientService;
import com.example.proyectospring.domain.model.PatientUI;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class RestPatient {

    private final PatientService patientService;

    public RestPatient(PatientService patientService) {
        this.patientService = patientService;
    }

    //Find all

    @GetMapping("/patients")
    public List<PatientUI> index() {
        return patientService.findAll();
    }

    //Find one

    @GetMapping("/patients/{id}")
    public Patient getPatient(@PathVariable int id)  {
        return patientService.get(id);
    }

    //Add

    @PostMapping("/patients")
    public int indexPost(@RequestBody PatientUI patientUi) {
        return patientService.add(patientUi);
    }

    //Update

    @PutMapping("/patients")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePatient(@RequestBody PatientUI patientUi) {
        patientService.update(patientUi);
    }

    //Delete

    @DeleteMapping("/patients/{patientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable int patientId, @RequestParam(required = false) boolean confirm) {
        patientService.delete(patientId, confirm);
    }
}



