package com.example.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.MQTT_config.InfluxDBConnection;
import com.influxdb.client.InfluxDBClient;

@Service
public class SensorService {
    private final String token = "VvWOj_-q0MV0jaRJTg8dreHYx0DvmukS2GDhRXIdUAC5ELM8WBp0F2IkRKIIjH7VW4E7uI0_u8XURwN9gKOyPA==";
    private final String org = "2d15c57353bf8d43";
    private final String bucket = "iot_data";
    private final String url = "https://us-east-1-1.aws.cloud2.influxdata.com/";
    private InfluxDBConnection influxDBConnection = new InfluxDBConnection();
    private InfluxDBClient influxDBClient = influxDBConnection.buildConnection(url, token, bucket, org);

    public String getSensorList() {
        return influxDBConnection.queryData(influxDBClient);
    }
}
