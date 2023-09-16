package com.example.lab2.domain.entities;

import lombok.Data;

@Data
public class BookEntity {
    private Long Id;
    private String title;
    private String author;
    private int publishingYear;
    private String publisher;
    private int numberOfPages;
}

