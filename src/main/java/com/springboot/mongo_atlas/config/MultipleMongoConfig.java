package com.springboot.mongo_atlas.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MultipleMongoConfig {
    @Primary
    @Bean(name = "db1Props")
    @ConfigurationProperties(prefix = "spring.data.mongodb.db1")
    public MongoProperties getNewDb1Props() throws Exception {
        return new MongoProperties();
    }

    @Bean(name = "db2Props")
    @ConfigurationProperties(prefix = "spring.data.mongodb.db2")
    public MongoProperties getNewDb2Props() throws Exception {
        return new MongoProperties();
    }

    @Primary
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(newdb1MongoDatabaseFactory(getNewDb1Props()));
    }

    @Bean(name = "db2Template")
    public MongoTemplate db2MongoTemplate() throws Exception {
        return new MongoTemplate(newdb2MongoDatabaseFactory(getNewDb2Props()));
    }

    @Primary
    @Bean
    public MongoDatabaseFactory newdb1MongoDatabaseFactory(@Qualifier("db1Props") MongoProperties mongo) throws Exception {
        return new SimpleMongoClientDatabaseFactory(
                mongo.getUri()
        );
    }

    @Bean
    public MongoDatabaseFactory newdb2MongoDatabaseFactory(@Qualifier("db2Props")MongoProperties mongo) throws Exception {
        return new SimpleMongoClientDatabaseFactory(
                mongo.getUri()
        );
    }

    /*@Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }*/

}
