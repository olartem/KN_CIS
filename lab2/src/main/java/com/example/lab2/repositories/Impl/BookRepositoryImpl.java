package com.example.lab2.repositories.Impl;

import com.example.lab2.domain.entities.BookEntity;
import com.example.lab2.repositories.BookRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private final JdbcTemplate jdbcTemplate;

    public BookRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<BookEntity> findById(Long id) {
        String sql = "SELECT id, title, author, publishing_year, publisher, num_pages FROM books WHERE id = ?";
        List<BookEntity> books = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> mapRowToBook(rs));
        return books.isEmpty() ? Optional.empty() : Optional.of(books.get(0));
    }

    @Override
    public Iterable<BookEntity> findAll() {
        String sql = "SELECT id, title, author, publishing_year, publisher, num_pages FROM books";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToBook(rs));
    }

    @Override
    public BookEntity save(BookEntity book) {
        String sql = "INSERT INTO books (title, author, publishing_year, publisher, num_pages) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getPublishingYear(),
                book.getPublisher(), book.getNumberOfPages());
        return book;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Iterable<BookEntity> findByAuthor(String author) {
        String sql = "SELECT id, title, author, publishing_year, publisher, num_pages FROM books " +
                "WHERE lower(author) LIKE lower(?)";
        return jdbcTemplate.query(sql, new Object[]{"%" + author + "%"}, (rs, rowNum) -> mapRowToBook(rs));
    }

    @Override
    public Iterable<BookEntity> findByPublisher(String publisher) {
        String sql = "SELECT id, title, author, publishing_year, publisher, num_pages FROM books " +
                "WHERE lower(publisher) LIKE lower(?)";
        return jdbcTemplate.query(sql, new Object[]{"%" + publisher + "%"}, (rs, rowNum) -> mapRowToBook(rs));
    }

    @Override
    public Optional<BookEntity> updateBook(BookEntity book) {
        String sql = "UPDATE books SET title = ?, author = ?, publishing_year = ?, publisher = ?, num_pages = ? WHERE id = ?";
        int result = jdbcTemplate.update(sql,
                book.getTitle(),
                book.getAuthor(),
                book.getPublishingYear(),
                book.getPublisher(),
                book.getNumberOfPages(),
                book.getId()
        );
        if(result == 0) {
            return Optional.empty();
        }
        else {
            return Optional.of(book);
        }
    }

    private BookEntity mapRowToBook(ResultSet rs) throws SQLException {
        BookEntity book = new BookEntity();
        book.setId(rs.getLong("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublishingYear(rs.getInt("publishing_year"));
        book.setPublisher(rs.getString("publisher"));
        book.setNumberOfPages(rs.getInt("num_pages"));
        return book;
    }

    // Implement other methods defined in the interface
}
