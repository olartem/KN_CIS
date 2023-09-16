package com.example.lab2.repositories.Impl;

import com.example.lab2.domain.entities.BookEntity;
import com.example.lab2.repositories.BookRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ListRepositoryImpl implements BookRepository {
    private final List<BookEntity> books = new ArrayList<>();
    private long nextId = 1;

    @Override
    public Optional<BookEntity> findById(Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }

    @Override
    public Iterable<BookEntity> findAll() {
        return books;
    }

    @Override
    public BookEntity save(BookEntity book) {
        if (book.getId() == null) {
            book.setId(nextId++);
        }
        books.add(book);
        return book;
    }
    @Override
    public Optional<BookEntity> updateBook(BookEntity book) {
        Optional<BookEntity> existingBook = findById(book.getId());
        if (existingBook.isPresent()) {
            books.remove(existingBook.get());
            books.add(book);
            return Optional.of(book);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {
        books.removeIf(book -> book.getId().equals(id));
    }

    @Override
    public Iterable<BookEntity> findByAuthor(String author) {
        return books.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .toList();
    }

    @Override
    public Iterable<BookEntity> findByPublisher(String publisher) {
        return books.stream()
                .filter(book -> book.getPublisher().toLowerCase().contains(publisher.toLowerCase()))
                .toList();
    }
}
