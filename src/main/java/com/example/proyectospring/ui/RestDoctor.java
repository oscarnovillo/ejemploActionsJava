package com.example.proyectospring.ui;

import com.example.proyectospring.domain.model.DoctorUI;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class RestDoctor {
 // TODO crear Doctor service y repository...
//    private final DoctorService doctorService;
//
//    public RestPatient(DoctorService doctorService) {
//        this.doctorService = doctorService;
//    }


    @GetMapping("/doctors")
    public List<DoctorUI> index() {
        return List.of(
            DoctorUI.builder().id(1).name("Doctor 1").build(),
            DoctorUI.builder().id(2).name("Doctor 2").build(),
            DoctorUI.builder().id(3).name("Doctor 3").build(),
            DoctorUI.builder().id(4).name("Doctor 4").build());

//        return doctorService.findAll();
    }

}
