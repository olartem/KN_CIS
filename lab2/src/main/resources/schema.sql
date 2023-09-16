DROP TABLE IF EXISTS "books";
CREATE TABLE books (
                       id SERIAL PRIMARY KEY,
                       title VARCHAR(255),
                       author VARCHAR(255),
                       publishing_year INTEGER,
                       publisher VARCHAR(255),
                       num_pages INTEGER
);