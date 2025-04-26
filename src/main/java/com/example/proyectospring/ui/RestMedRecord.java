package com.example.proyectospring.ui;

import com.example.proyectospring.domain.model.MedRecordUI;
import com.example.proyectospring.domain.services.MedRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class RestMedRecord {

    private final MedRecordService medRecordService;

    public RestMedRecord(MedRecordService medRecordService) {
        this.medRecordService = medRecordService;
    }

    //Find patient medRecords
   // @CrossOrigin(origins = "http://127.0.0.1:5500")
    @GetMapping("/patients/{idPatient}/medRecords")
    public List<MedRecordUI> getAll(@PathVariable int idPatient)  {
        return medRecordService.getAll(idPatient);
    }

    //Add
   // @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PostMapping("/patients/medRecords")
    public int indexPost(@RequestBody MedRecordUI medRecordui) {

        return medRecordService.add(medRecordui);

    }

    //Delete
    //@CrossOrigin(origins = "http://127.0.0.1:5500")
    @DeleteMapping("/patients/medRecords/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedRecord(@PathVariable int id) {
        medRecordService.delete(id);
    }

    //Update
    //@CrossOrigin(origins = "http://127.0.0.1:5500")
    @PutMapping("/patients/medRecords")
    @ResponseStatus(HttpStatus.NO_CONTENT)
        public void updateMedRecord(@RequestBody MedRecordUI medRecordUI) {
        medRecordService.update(medRecordUI);
    }
}

