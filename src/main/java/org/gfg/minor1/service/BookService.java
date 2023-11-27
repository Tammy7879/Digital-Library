package org.gfg.minor1.service;

import org.gfg.minor1.models.*;
import org.gfg.minor1.repository.AuthorRepository;
import org.gfg.minor1.repository.BookRepository;
import org.gfg.minor1.request.CreateBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public void create(CreateBookRequest createBookRequest) {
        Book book = createBookRequest.to();
        // condition that we need to check whether we need to save it in our db or not

        // here u need to have same method present in author repo which will tell
        Author authorFromDB = authorRepository.findByEmail(book.getAuthor().getEmail());
        if(authorFromDB == null){
            authorFromDB = authorRepository.save(book.getAuthor());
        }

        bookRepository.save(book);
    }
    public Book createUpdate(Book book){
        return bookRepository.save(book);
    }

    public List<Book> findBooks(BookFilterType bookFiltertype, String value, OperationType operationType) {
        switch (operationType){
            case EQUALS:
                switch (bookFiltertype){
                    case BOOK_NO:
                        return bookRepository.findByBookNo(value);
                    case AUTHOR_NAME:
                        return bookRepository.findByAuthor_Name(value);
                    case COST:
                        return bookRepository.findByCost(Integer.valueOf(value));
                    case BOOKTYPE:
                        return bookRepository.findByType(BookType.valueOf(value));
                }
            case LESS_THAN:
                switch (bookFiltertype){
                    case COST:
                        return bookRepository.findByCostLessThan(Integer.valueOf(value));
                }
            case GREATER_THAN:
                switch (bookFiltertype){
                    case COST:
                        return bookRepository.findByCostGreaterThan(Integer.valueOf(value));
                }
            default:
                return new ArrayList<>();
        }
    }
}
