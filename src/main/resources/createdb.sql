-- Таблица авторов
CREATE TABLE Authors (
                         author_id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         birth_year INT
);

-- Таблица книг
CREATE TABLE Books (
                       book_id SERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author_id INT,
                       year_published INT,
                       genre VARCHAR(100),
                       FOREIGN KEY (author_id) REFERENCES Authors(author_id) ON DELETE SET NULL
);

-- Таблица читателей
CREATE TABLE Readers (
                         reader_id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         email VARCHAR(255) UNIQUE,
                         registration_date DATE DEFAULT CURRENT_DATE
);

-- Таблица выдач книг
CREATE TABLE Loans (
                       loan_id SERIAL PRIMARY KEY,
                       book_id INT,
                       reader_id INT,
                       loan_date DATE DEFAULT CURRENT_DATE,
                       return_date DATE,
                       FOREIGN KEY (book_id) REFERENCES Books(book_id) ON DELETE CASCADE,
                       FOREIGN KEY (reader_id) REFERENCES Readers(reader_id) ON DELETE CASCADE
);
