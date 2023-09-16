package com.example.lab2.repositories;

import com.example.lab2.domain.entities.BookEntity;

import java.util.Optional;

public interface BookRepository {
    Optional<BookEntity> findById(Long id);
    Iterable<BookEntity> findAll();
    BookEntity save(BookEntity book);
    Optional<BookEntity> updateBook(BookEntity book);
    void deleteById(Long id);
    Iterable<BookEntity> findByAuthor(String author);
    Iterable<BookEntity> findByPublisher(String publisher);
}
