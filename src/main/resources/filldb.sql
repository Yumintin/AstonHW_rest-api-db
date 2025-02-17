-- Вставка тестовых данных
INSERT INTO Authors (name, birth_year) VALUES ('Лев Толстой', 1828), ('Фёдор Достоевский', 1821);
INSERT INTO Books (title, author_id, year_published, genre) VALUES ('Война и мир', 1, 1869, 'Роман'), ('Преступление и наказание', 2, 1866, 'Роман');
INSERT INTO Readers (name, email) VALUES ('Иван Иванов', 'ivan@example.com'), ('Мария Петрова', 'maria@example.com');
INSERT INTO Loans (book_id, reader_id, loan_date, return_date) VALUES (1, 1, '2025-02-17', '2025-03-01');
