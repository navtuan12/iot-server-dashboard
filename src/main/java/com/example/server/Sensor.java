package com.example.server;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private Date timestamp;
    private Double temperature;
    private Double humidity;

    public Sensor(Date date ,Double temperature, Double humidity){
        this.temperature = temperature;
        this.humidity = humidity;
        this.timestamp = date;
    }
}