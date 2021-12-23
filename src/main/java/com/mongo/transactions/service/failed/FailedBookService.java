package com.mongo.transactions.service.failed;

import com.mongo.transactions.model.Book;
import com.mongo.transactions.model.BookStatistic;
import com.mongo.transactions.repos.BookRepository;
import com.mongo.transactions.repos.BookStatisticRepository;
import com.mongo.transactions.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Profile("failed")
@Service
@RequiredArgsConstructor
public class FailedBookService implements BookService {

    private final BookRepository bookRepository;
    private final BookStatisticRepository bookStatisticRepository;

    @Transactional
    public Book saveBook(Book book) {
        bookRepository.save(book);
        var bookStatistic = bookStatisticRepository.findByName(book.getGenre())
                .map(bs -> {
                    bs.incrementValue();
                    return bs;
                })
                .orElse(BookStatistic.builder().name(book.getGenre()).value(1L).build());
        bookStatisticRepository.save(bookStatistic);
        return book;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void deleteAll() {
        bookRepository.deleteAll();
        bookStatisticRepository.deleteAll();
    }
}
