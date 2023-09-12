package com.example.lab2.services;

import com.example.lab2.domain.entities.BookEntity;
import com.example.lab2.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Iterable<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<BookEntity> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public BookEntity createBook(BookEntity book) {
        return bookRepository.save(book);
    }

    public Optional<BookEntity> updateBook(Long id, BookEntity updatedBook) {
        Optional<BookEntity> existingBook = bookRepository.findById(id);
        if (existingBook.isPresent()) {
            updatedBook.setId(id);
            return Optional.of(bookRepository.save(updatedBook));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteBook(Long id) {
        Optional<BookEntity> existingBook = bookRepository.findById(id);
        if (existingBook.isPresent()) {
            bookRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    public Iterable<BookEntity> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorIgnoreCaseContaining(author);
    }

    public Iterable<BookEntity> getBooksByPublisher(String publisher) {
        return bookRepository.findByPublisherIgnoreCaseContaining(publisher);
    }
}
