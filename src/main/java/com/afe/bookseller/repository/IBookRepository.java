package com.afe.bookseller.repository;

import com.afe.bookseller.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IBookRepository extends JpaRepository<Book, Long> {
}
