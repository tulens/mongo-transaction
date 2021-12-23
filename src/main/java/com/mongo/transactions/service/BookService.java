package com.mongo.transactions.service;

import com.mongo.transactions.model.Book;

import java.util.List;

public interface BookService {

    Book saveBook(Book book);

    List<Book> getAllBooks();

    void deleteAll();
}
