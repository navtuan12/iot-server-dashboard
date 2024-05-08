package com.example.server;

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
        query.with(Sort.by(Sort.Direction.DESC, "date"));
        query.limit(10);
        List<Sensor> latestSensor = mongoTemplate.find(query, Sensor.class);

        String tempList;
        tempList = "[";
        for(Sensor sensor : latestSensor){
            tempList += "{\"timestamp\":" + "\"" + sensor.getTimestamp() + "\"" + ",\n \"temperature\": " + sensor.getTemperature() + ",\n \"humidity\": " + sensor.getHumidity() + "}\n,";   
        }
        tempList = tempList.substring(0, tempList.length() - 1);
        tempList += "]";
        return tempList;
    }

}
