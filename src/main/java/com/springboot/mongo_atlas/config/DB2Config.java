package com.springboot.mongo_atlas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.springboot.mongo_atlas.db2"},
        mongoTemplateRef = DB2Config.MONGO_TEMPLATE
)
public class DB2Config {
    protected static final String MONGO_TEMPLATE = "db2Template";
}
