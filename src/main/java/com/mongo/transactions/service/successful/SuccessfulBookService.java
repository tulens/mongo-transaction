package com.mongo.transactions.service.successful;

import com.mongo.transactions.model.Book;
import com.mongo.transactions.model.BookStatistic;
import com.mongo.transactions.service.BookService;
import com.mongodb.ClientSessionOptions;
import com.mongodb.MongoException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.TransactionBody;
import com.mongodb.client.internal.ClientSessionClock;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.mongodb.MongoException.TRANSIENT_TRANSACTION_ERROR_LABEL;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Profile("successful")
@Service
@RequiredArgsConstructor
public class SuccessfulBookService implements BookService {
    private static final int MAX_RETRY_MS = 120000;

    private final MongoDatabaseFactory mongoDatabaseFactory;
    private final MongoTemplate mongoTemplate;
    private final ApplicationContext appContext;

    public Book saveBook(Book book) {

        var options = ClientSessionOptions.builder().build();
        var session = mongoDatabaseFactory.getSession(options);
        try (session) {
            var txBody = saveBookAndIncrementCounter(session, book);
            return executeTransaction(session, txBody);
        }
    }

    private TransactionBody<Book> saveBookAndIncrementCounter(ClientSession session, Book book) {
        return () -> {

            var template = mongoTemplate.withSession(session);
            template.setApplicationContext(appContext);

            var savedBook = template.insert(book);

            var bookStatistic = findStatisticByName(book.getGenre())
                    .map(bs -> {
                        bs.incrementValue();
                        return bs;
                    })
                    .orElse(BookStatistic.builder().name(book.getGenre()).value(1L).build());
            template.save(bookStatistic);
            return savedBook;
        };
    }

    private Book executeTransaction(ClientSession session, TransactionBody<Book> txBody) {
        var start = ClientSessionClock.INSTANCE.now();
        while (true) {
            try {
                return session.withTransaction(txBody);
            } catch (RuntimeException ex) {

                if (isTransientTransactionException(ex)
                        && ClientSessionClock.INSTANCE.now() - start < MAX_RETRY_MS) {
                    continue;
                }
                throw ex;
            }
        }
    }

    private boolean isTransientTransactionException(RuntimeException ex) {
        return ex.getCause() instanceof MongoException mongoExceptionCause
                && mongoExceptionCause.hasErrorLabel(TRANSIENT_TRANSACTION_ERROR_LABEL);
    }

    private Optional<BookStatistic> findStatisticByName(String name) {
        var book = mongoTemplate.findOne(
                new Query(where("name").is(name)),
                BookStatistic.class
        );
        return Optional.ofNullable(book);
    }

    public List<Book> getAllBooks() {
        return mongoTemplate.findAll(Book.class);
    }

    public void deleteAll() {
        mongoTemplate.remove(new Query(), Book.class);
        mongoTemplate.remove(new Query(), BookStatistic.class);
    }
}
