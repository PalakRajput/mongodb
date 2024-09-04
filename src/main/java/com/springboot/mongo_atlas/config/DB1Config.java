package com.springboot.mongo_atlas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.springboot.mongo_atlas.db1"},
        mongoTemplateRef = DB1Config.MONGO_TEMPLATE
)
public class DB1Config {
    protected static final String MONGO_TEMPLATE = "mongoTemplate";
}
