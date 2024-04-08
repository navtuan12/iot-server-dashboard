package com.example.server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/sensors")
public class SensorController {
    

    @Autowired
    private SensorService service;
    
    @GetMapping("/dashboard")
    public ResponseEntity<String> getSensorData() {
        return new ResponseEntity<String>(service.getSensorList(), HttpStatus.OK);
    }
}
