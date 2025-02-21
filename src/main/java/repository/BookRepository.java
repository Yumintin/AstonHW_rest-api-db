package repository;

import entity.Book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    private static final String SQL_INSERT = "INSERT INTO books (title, author_id, year_published, genre) VALUES (?,?,?,?) RETURNING book_id";
    private static final String SQL_SELECT_BY_ID = "SELECT book_id,title,author_id,year_published,genre FROM books WHERE book_id = ?";
    private static final String SQL_UPDATE = "UPDATE books SET title=?, author_id=?, year_published=?, genre=? WHERE book_id=?";
    private static final String SQL_SELECT_ALL = "SELECT book_id,title,author_id,year_published,genre FROM books";
    private static final String SQL_DELETE = "DELETE FROM books WHERE book_id = ?";


    /**
     * Создаем новую запись Book в базе данных
     *
     * @param book объект Book для создания
     * @return созданный объект Book с заполненным bookId
     */
    public Book create(Book book) {
        return DBConnection.executePreparedStatement(SQL_INSERT,
                stmt -> fillPreparedStatementForBook(stmt, book, false),
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            book.setBookId(rs.getInt("book_id"));
                        }
                    }
                    return book;
                });
    }

    /**
     * Находит запись Book в базе данных
     *
     * @param id идентификатор книги
     * @return объект Book, если запись найдена, иначе null
     */
    public Book findByBookId(int id) {
        return DBConnection.executePreparedStatement(SQL_SELECT_BY_ID,
                stmt -> stmt.setInt(1, id),
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            return mapResultSetToBook(rs);
                        }
                    }
                    return null;
                });
    }

    /**
     * Обновляет существующую запись Book
     *
     * @param book объект Book с обновеллынми данными
     * @return true, если обновление прошло успешно, иначе false
     */
    public boolean update(Book book) {
        return DBConnection.executePreparedStatement(SQL_UPDATE,
                stmt -> fillPreparedStatementForBook(stmt, book, true),
                stmt -> stmt.executeUpdate() > 0
        );
    }

    /**
     * Возвращает список всех записей Book из базы данных.
     *
     * @return список всех объектов Book
     */
    public List<Book> findAll() {
        return DBConnection.executePreparedStatement(SQL_SELECT_ALL,
                stmt -> {
                },
                stmt -> {
                    List<Book> books = new ArrayList<>();
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            books.add(mapResultSetToBook(rs));
                        }
                    }
                    return books;
                });
    }

    /**
     * Удаляет запись Book по заданному идентификатору.
     *
     * @param id идентификатор книги
     * @return true, если удаление прошло успешно, иначе false
     */
    public boolean delete(int id) {
        return DBConnection.executePreparedStatement(SQL_DELETE,
                stmt -> stmt.setInt(1, id),
                stmt -> stmt.executeUpdate() > 0);
    }

    /**
     * Заполняет объект PreparedStatement Значениями из объекта Book
     *
     * @param stmt     PreparedStatement, который нужно заполнить
     * @param book     объект Book, содержащий данные
     * @param isUpdate true, если операция обновления, false для операции вставки
     * @throws SQLException в случае ошибки доступа к базе данных
     */
    private void fillPreparedStatementForBook(PreparedStatement stmt, Book book, boolean isUpdate) throws SQLException {
        stmt.setString(1, book.getTitle());
        stmt.setObject(2, book.getAuthorId(), java.sql.Types.INTEGER);
        stmt.setObject(3, book.getYearPublished(), java.sql.Types.INTEGER);
        stmt.setString(4, book.getGenre());
        if (isUpdate) {
            stmt.setInt(5, book.getBookId());
        }
    }

    /**
     * Преобразует текущую строку объекта ResultSet  объект Book
     *
     * @param rs объект ResultSet, уже установленный на нужную строку
     * @return объект Book, полученный из данных ResultSet
     * @throws SQLException в случае ошибки доступа к базе данных
     */
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBookId(rs.getInt("book_id"));
        book.setTitle(rs.getString("title"));
        book.setAuthorId((Integer) rs.getObject("author_id"));
        book.setYearPublished((Integer) rs.getObject("year_published"));
        book.setGenre(rs.getString("genre"));
        return book;
    }
}
