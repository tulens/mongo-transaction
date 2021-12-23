package com.mongo.transactions.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@Builder
@Document(collection = "books")
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    private String id;

    private String name;

    private String genre;

    @Version
    private Long version;

    @CreatedDate
    private Date createDate;

    @LastModifiedDate
    private Date modificationDate;
}
