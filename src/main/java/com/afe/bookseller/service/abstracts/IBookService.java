package com.afe.bookseller.service.abstracts;

import com.afe.bookseller.model.Book;

import java.util.List;


public interface IBookService
{
    Book saveBook(Book book);

    void deleteBook(Long id);

    List<Book> findAllBooks();
}
