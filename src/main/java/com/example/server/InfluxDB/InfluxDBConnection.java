package com.example.server.InfluxDB;

import java.time.Instant;
import java.time.OffsetDateTime;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.decimal4j.util.DoubleRounder;
import org.springframework.beans.factory.annotation.Autowired;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.example.server.Model.Sensor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

            Sensor sensor = new Sensor(
                    Instant.now(),
                    DoubleRounder.round(tempValue, 2),
                    DoubleRounder.round(humidityValue, 2));

            writeApi.writeMeasurement(WritePrecision.NS, sensor);
            flag = true;
        } catch (
        InfluxException e) {
            System.out.println("Exception!!" + e.getMessage());
        }
        return flag;
    }

    public String queryData(InfluxDBClient influxDBClient) {
        String queryString = "[";
        String sql =  "from(bucket:\"iot_data\") |> range(start:0) |> filter(fn: (r) => r[\"_measurement\"] == \"sensor\") |> filter(fn: (r) => r[\"_field\"] == \"humidity\") |> sort() |> limit(n:10)";
        
        List<FluxTable> tables = influxDBClient.getQueryApi().query(sql);
        for (FluxTable fluxTable : tables) {
            List<FluxRecord> records = fluxTable.getRecords();
            for (FluxRecord fluxRecord : records) {
                queryString += "{ \"time\":" + "\"" + fluxRecord.getValueByIndex(4) + "\"" + ",\n \"bid\":" + fluxRecord.getValueByIndex(5) + "},";
            }
        }
        queryString = queryString.substring(0, queryString.length()-1);
        queryString += "]";
        return queryString;
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
