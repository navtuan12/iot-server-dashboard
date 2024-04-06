package com.example.server.MQTT_config;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.decimal4j.util.DoubleRounder;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.exceptions.InfluxException;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.influxdb.client.DeleteApi;

public class InfluxDBConnection {

    private String token;
    private String bucket;
    private String org;
    private String url;

    public InfluxDBClient buildConnection(String url, String token, String bucket, String org) {
        setToken(token);
        setBucket(bucket);
        setOrg(org);
        setUrl(url);
        return InfluxDBClientFactory.create(getUrl(), getToken().toCharArray(), getOrg(), getBucket());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean writePointbyPOJO(InfluxDBClient influxDBClient, String msg) {
        boolean flag = false;
        try {
            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(msg, JsonObject.class);
            Double tempValue = Double.parseDouble(obj.get("temp").toString());
            Double humidityValue = Double.parseDouble(obj.get("humidity").toString());

            Sensor sensor = new Sensor();

            sensor.temperature = DoubleRounder.round(tempValue, 2);
            sensor.humidity = DoubleRounder.round(humidityValue, 2);
            sensor.time = Instant.now();

            writeApi.writeMeasurement(WritePrecision.NS, sensor);
            flag = true;
        } catch (

        InfluxException e) {
            System.out.println("Exception!!" + e.getMessage());
        }
        return flag;
    }

    @Measurement(name = "sensor")
    private static class Sensor {

        @Column
        Double temperature;

        @Column
        Double humidity;

        @Column(timestamp = true)
        Instant time;
    }

    public void queryData(InfluxDBClient influxDBClient) {
        String flux = "from(bucket:\"myFirstBucket\") |> range(start:0) |> filter(fn: (r) => r[\"_measurement\"] == \"sensor\") |> filter(fn: (r) => r[\"sensor_id\"] == \"TLM0100\"or r[\"sensor_id\"] == \"TLM0101\" or r[\"sensor_id\"] == \"TLM0103\" or r[\"sensor_id\"] == \"TLM0200\") |> sort() |> yield(name: \"sort\")";
        // from(bucket: "myFirstBucket")
        // |> range(start: v.timeRangeStart, stop: v.timeRangeStop)
        // |> filter(fn: (r) => r["_measurement"] == "sensor")
        // |> filter(fn: (r) => r["_field"] == "model_number")
        // |> filter(fn: (r) => r["sensor_id"] == "TLM0100" or r["sensor_id"] ==
        // "TLM0101" or r["sensor_id"] == "TLM0103" or r["sensor_id"] == "TLM0200")
        // |> sort()
        // |> yield(name: "sort")

        QueryApi queryApi = influxDBClient.getQueryApi();

        List<FluxTable> tables = queryApi.query(flux);
        for (FluxTable fluxTable : tables) {
            List<FluxRecord> records = fluxTable.getRecords();
            for (FluxRecord fluxRecord : records) {
                System.out.println(fluxRecord.getValueByKey("sensor_id"));
            }
        }
    }

    public boolean deleteRecord(InfluxDBClient influxDBClient) {
        boolean flag = false;
        DeleteApi deleteApi = influxDBClient.getDeleteApi();

        try {

            OffsetDateTime start = OffsetDateTime.now().minus(72, ChronoUnit.HOURS);
            OffsetDateTime stop = OffsetDateTime.now();
            String predicate = "_measurement=\"sensor\" AND sensor_id = \"TLM0201\"";

            deleteApi.delete(start, stop, predicate, "myFirstBucket", "TestOrg");

            flag = true;
        } catch (InfluxException ie) {
            System.out.println("InfluxException: " + ie);
        }
        return flag;
    }

}
