package com.example.server.Model;

import java.time.Instant;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Measurement(name = "sensor")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sensor {

    @Column(timestamp = true)
    Instant time;

    @Column
    Double temperature;

    @Column
    Double humidity;
}