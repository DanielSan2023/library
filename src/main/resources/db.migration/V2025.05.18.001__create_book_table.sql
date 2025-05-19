CREATE TABLE IF NOT EXISTS book (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(255) NOT NULL UNIQUE,
                      author VARCHAR(255) NOT NULL,
                      isbn VARCHAR(255) NOT NULL UNIQUE,
                      published_year INT NOT NULL
);

CREATE TABLE IF NOT EXISTS book_copy (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           book_id BIGINT NOT NULL,
                           available TINYINT(1) NOT NULL DEFAULT 1,
                           CONSTRAINT fk_book_copy_book FOREIGN KEY (book_id) REFERENCES book(id)
);