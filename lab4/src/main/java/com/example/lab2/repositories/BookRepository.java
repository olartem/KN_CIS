package com.example.lab2.repositories;

import com.example.lab2.domain.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Iterable<BookEntity> findByAuthorIgnoreCaseContaining(String author);
    Iterable<BookEntity> findByPublisherIgnoreCaseContaining(String publisher);
}
