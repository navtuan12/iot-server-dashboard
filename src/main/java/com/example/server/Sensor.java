package com.example.server;

import java.sql.Date;
import java.sql.Timestamp;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TimeSeries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "sensor_time_series")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sensor {
    @Id
    private ObjectId id;
    private String timestamp;
    private Double temperature;
    private Double humidity;

    public Sensor(String date ,Double temperature, Double humidity){
        this.temperature = temperature;
        this.humidity = humidity;
        this.timestamp = date;
    }
}