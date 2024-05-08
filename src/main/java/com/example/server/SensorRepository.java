package com.example.server;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends MongoRepository<Sensor, ObjectId> {
    Sensor findById(String id);
}
