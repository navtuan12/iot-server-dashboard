package com.example.server;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

@Service
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public String getSensorData() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "timestamp"));
        query.limit(10);
        List<Sensor> latestSensor = mongoTemplate.find(query, Sensor.class);
        Collections.reverse(latestSensor);
        
        String tempList = "[";
        for(Sensor sensor : latestSensor){
            String timeFix = sensor.getTimestamp().toString().substring(4,19);
            tempList += "{\"timestamp\":" + "\"" + timeFix + "\"" + ",\n \"temperature\": " + sensor.getTemperature() + ",\n \"humidity\": " + sensor.getHumidity() + "}\n,";   
        }
        tempList = tempList.substring(0, tempList.length() - 1);
        tempList += "]";
        return tempList;
    }

    public String getGaugeData(){
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "timestamp"));
        query.limit(1);
        List<Sensor> latestSensor = mongoTemplate.find(query, Sensor.class);

        String gaugeData = "{";
        String timeFix = latestSensor.get(0).getTimestamp().toString().substring(4,19);
        gaugeData += "\"timestamp\":" + "\"" + timeFix + "\"" + ",\"temperature\": " + latestSensor.get(0).getTemperature() + ", \"humidity\": " + latestSensor.get(0).getHumidity() + "}";

        return gaugeData;
    }
}
