package com.springboot.mongo_atlas.config;

import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.springboot.mongo_atlas.db2.entity.Student;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class MongoChangeStreamConfig {
    @Value("${spring.data.mongodb.db2.uri}")
    String uri;

    public void changeStream() {

        // connect to the local database server
        MongoDatabase database;
        //try with resources will close the connection as soon as the try block is executed.
        MongoClient mongoClient = MongoClients.create(uri);

        // Select the MongoDB database
        database = mongoClient.getDatabase("sample_mflix");


        // Select the collection to query
        MongoCollection<Student> collection = database.getCollection("demo", Student.class);

        // Create pipeline for operationType filter
        List<Bson> pipeline = List.of(
                Aggregates.match(
                        Filters.in(
                                "operationType",
                                Arrays.asList("insert", "update", "delete")
                        )));

        // Create the Change Stream
        ChangeStreamIterable<Student> changeStream = collection.watch(pipeline)
                .fullDocument(FullDocument.UPDATE_LOOKUP);

        // Iterate over the Change Stream
        for (ChangeStreamDocument<Student> changeEvent : changeStream) {
            // Process the change event here
            Student document = changeEvent.getFullDocument();
            System.out.println("Time: " + changeEvent.getClusterTime());
            switch (changeEvent.getOperationType()) {
                case INSERT:
                    System.out.println("MongoDB Change Stream detected an insert");
                    break;
                case UPDATE:
                    System.out.println(changeEvent.getUpdateDescription().getUpdatedFields());
                    System.out.println(changeEvent.getUpdateDescription().getRemovedFields());
                    System.out.println("MongoDB Change Stream detected an update");
                    break;
                case DELETE:
                    System.out.println("MongoDB Change Stream detected a delete");
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + changeEvent.getOperationType());
            }
        }
    }
}
