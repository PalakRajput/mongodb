package com.springboot.mongo_atlas;

import com.springboot.mongo_atlas.config.MongoChangeStreamConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class MongoAtlasApplication {
	@Autowired
	private MongoChangeStreamConfig changeStreamConfig;

	public static void main(String[] args) {
		SpringApplication.run(MongoAtlasApplication.class, args);
	}

	@PostConstruct
	public void init(){
		changeStreamConfig.changeStream();
	}
}
