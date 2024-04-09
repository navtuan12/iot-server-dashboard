package com.example.server.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.Service.SensorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/sensors")
@CrossOrigin(origins = "http://localhost:3000")
public class SensorController {
    

    @Autowired
    private SensorService service;
    
    @GetMapping("/dashboard")
    public ResponseEntity<String> getSensorData() {
        return new ResponseEntity<String>(service.getSensorList(), HttpStatus.OK);
    }
}
