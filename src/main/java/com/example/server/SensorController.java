package com.example.server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/sensors")
@CrossOrigin(origins = "http://127.0.0.1:5173")
public class SensorController {
    
    @Autowired
    private SensorService service;
    
    @GetMapping("/dashboard/chart")    
    public ResponseEntity<String> getTempData() {
        return new ResponseEntity<String>(service.getSensorData(), HttpStatus.OK);
    }

    @GetMapping("/dashboard/gauge")
    public ResponseEntity<String> getGaugeData() {
        return new ResponseEntity<String>(service.getGaugeData(), HttpStatus.OK);
    }
}
