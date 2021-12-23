package com.mongo.transactions.repos;

import com.mongo.transactions.model.BookStatistic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookStatisticRepository extends MongoRepository<BookStatistic, String> {

    Optional<BookStatistic> findByName(String name);
}
