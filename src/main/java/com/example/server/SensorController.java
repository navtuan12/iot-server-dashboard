package com.example.server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.github.kokorin.jaffree.StreamType;
import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.PipeOutput;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    // @PostMapping("/dashboard/video")
    // public ResponseEntity<String> receiveFrame(@RequestParam("files") MultipartFile file) {
    //     if (file.isEmpty()) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file received");
    //     }

    //     try {
    //         // Save the file locally
    //         byte[] bytes = file.getBytes();
    //         Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
    //         Files.write(path, bytes);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving file");
    //     }

    //     return ResponseEntity.ok("File received successfully");
    // }
}
