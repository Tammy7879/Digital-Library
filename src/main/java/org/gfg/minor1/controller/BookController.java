package org.gfg.minor1.controller;

import org.gfg.minor1.models.Book;
import org.gfg.minor1.models.BookFilterType;
import org.gfg.minor1.models.OperationType;
import org.gfg.minor1.request.CreateBookRequest;
import org.gfg.minor1.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping(value = "/create")
    public void create(@RequestBody CreateBookRequest createBookRequest){
        bookService.create(createBookRequest);
    }

    @GetMapping("/find")
    public List<Book> findBooks(@RequestParam("filter") BookFilterType bookFiltertype,
                                @RequestParam("value") String value,
                                @RequestParam("operation")OperationType operationType){
     return bookService.findBooks(bookFiltertype, value, operationType);
    }

}
