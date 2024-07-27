package com.example.server;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "motorcycle_count")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MotorcycleCount {
    @Id
    private ObjectId id;
    private Date timestamp;
    private int count;

    public MotorcycleCount(Date date ,int count){
        this.count = count;
        this.timestamp = date;
    }
}