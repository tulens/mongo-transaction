package com.mongo.transactions.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "book_statistics")
public class BookStatistic {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private Long value;

    @CreatedDate
    private Date createDate;

    @LastModifiedDate
    private Date modificationDate;

    public void incrementValue() {
        this.value++;
    }
}
