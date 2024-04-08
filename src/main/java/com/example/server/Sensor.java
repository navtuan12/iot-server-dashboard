package com.example.server;

import java.time.Instant;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

@Measurement(name = "sensor")
public class Sensor {

    @Column(timestamp = true)
    Instant time;

    @Column
    Double temperature;

    @Column
    Double humidity;

    public Sensor(Instant time, Double temperature, Double humidity) {
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
    }
}