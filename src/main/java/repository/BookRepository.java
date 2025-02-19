package repository;

import entity.Book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    //CREATE
    public Book create(Book book) {
        String sql = "INSERT INTO books (title, author_id, year_published, genre) " +
                "VALUES (?,?,?,?) RETURNING book_id";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> fillPreparedStatementForBook(preparedStatement, book, false),
                preparedStatement -> {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            book.setBook_id(resultSet.getInt("book_id"));
                        }
                    }
                    return book;
                });
    }

    //READ
    public Book findByBookId(int id) {
        String sql = "SELECT book_id,title,author_id,year_published,genre FROM books WHERE book_id = ?";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> preparedStatement.setInt(1, id),
                preparedStatement -> {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            return mapResultSetToBook(resultSet);
                        }
                    }
                    return null;
                });
    }

    //UPDATE
    public boolean update(Book book) {
        String sql = "UPDATE books SET title=?, author_id=?, year_published=?, genre=? WHERE book_id=?";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> fillPreparedStatementForBook(preparedStatement, book, true),
                preparedStatement -> preparedStatement.executeUpdate() > 0
        );
    }

    //READ ALL
    public List<Book> findAll() {
        String sql = "SELECT book_id,title,author_id,year_published,genre FROM books";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> {
                },
                preparedStatement -> {
                    List<Book> books = new ArrayList<>();
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            books.add(mapResultSetToBook(resultSet));
                        }
                    }
                    return books;
                });
    }

    //DELETE
    public boolean delete(int id) {
        String sql = "DELETE FROM books WHERE book_id = ?";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> preparedStatement.setInt(1, id),
                preparedStatement -> preparedStatement.executeUpdate() > 0
        );
    }

    private void fillPreparedStatementForBook(PreparedStatement stmt, Book book, boolean isUpdate) throws SQLException {
        stmt.setString(1, book.getTitle());
        if (book.getAuthor_id() != null) {
            stmt.setInt(2, book.getAuthor_id());
        } else {
            stmt.setNull(2, java.sql.Types.INTEGER);
        }
        if (book.getYear_published() != null) {
            stmt.setInt(3, book.getYear_published());
        } else {
            stmt.setNull(3, java.sql.Types.INTEGER);
        }
        stmt.setString(4, book.getGenre());

        if (isUpdate) {
            if (book.getAuthor_id() == null) {
                throw new IllegalArgumentException("Book ID cannot be null for update");
            }
            stmt.setInt(5, book.getBook_id());
        }
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBook_id(rs.getInt("book_id"));
        book.setTitle(rs.getString("title"));

        int author_id = rs.getInt("author_id");
        if (rs.wasNull()) {
            book.setAuthor_id(null);
        } else {
            book.setAuthor_id(author_id);
        }

        int year_published = rs.getInt("year_published");
        if (rs.wasNull()) {
            book.setYear_published(null);
        } else {
            book.setYear_published(year_published);
        }

        book.setGenre(rs.getString("genre"));
        return book;
    }
}
