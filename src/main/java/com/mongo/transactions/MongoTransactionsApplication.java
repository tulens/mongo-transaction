package com.mongo.transactions;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import javax.annotation.PostConstruct;

@EnableMongoAuditing
@SpringBootApplication
@RequiredArgsConstructor
public class MongoTransactionsApplication {

    private final MongoTemplate mongoTemplate;

    public static void main(String[] args) {
        SpringApplication.run(MongoTransactionsApplication.class, args);
    }

    @PostConstruct
    private void init() {
        mongoTemplate.indexOps("book_statistics").ensureIndex(new Index("name", Sort.Direction.ASC).unique());
    }
}
