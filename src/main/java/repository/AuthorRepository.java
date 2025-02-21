package repository;

import entity.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepository {

    private static final String SQL_INSERT = "INSERT INTO authors(name, birth_year) values(?,?) RETURNING author_id";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM authors WHERE author_id = ?";
    private static final String SQL_UPDATE = "UPDATE authors SET name = ?, birth_year = ? WHERE author_id = ?";
    private static final String SQL_SELECT_ALL = "SELECT author_id,name,birth_year FROM authors";
    private static final String SQL_DELETE = "DELETE FROM authors WHERE author_id = ?";


    /**
     * Создаем новую запись Author в базе данных
     *
     * @param author объект Author для создания
     * @return созданный объект Author с заполненным authorId
     */
    public Author create(Author author) {
        return DBConnection.executePreparedStatement(SQL_INSERT,
                stmt -> fillPreparedStatementForAuthor(stmt, author, false),
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            author.setAuthorId(rs.getInt("author_id"));
                        }
                    }
                    return author;
                }
        );
    }

    /**
     * Находит запись Author в базе данных
     *
     * @param id идентификатор автора
     * @return объект Author, если запись найдена, иначе null
     */
    public Author findbyId(int id) {
        return DBConnection.executePreparedStatement(SQL_SELECT_BY_ID,
                stmt -> stmt.setInt(1, id),
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        return rs.next() ? mapResultSetToAuthor(rs) : null;
                    }
                }
        );
    }

    /**
     * Обновляет существующую запись Author
     *
     * @param author объект Author с обновеллынми данными
     * @return true, если обновление прошло успешно, иначе false
     */
    public boolean update(Author author) {
        return DBConnection.executePreparedStatement(SQL_UPDATE,
                stmt -> fillPreparedStatementForAuthor(stmt, author, true),
                stmt -> stmt.executeUpdate() > 0
        );
    }

    /**
     * Возвращает список всех записей Author из базы данных.
     *
     * @return список всех объектов Author
     */
    public List<Author> findAll() {
        return DBConnection.executePreparedStatement(SQL_SELECT_ALL,
                stmt -> {
                },
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        List<Author> authors = new ArrayList<>();
                        while (rs.next()) {
                            authors.add(mapResultSetToAuthor(rs));
                        }
                        return authors;
                    }
                }
        );
    }

    /**
     * Удаляет запись Author по заданному идентификатору.
     *
     * @param id идентификатор автора
     * @return true, если удаление прошло успешно, иначе false
     */
    public boolean delete(int id) {
        return DBConnection.executePreparedStatement(SQL_DELETE,
                preparedStatement -> preparedStatement.setInt(1, id),
                preparedStatement -> preparedStatement.executeUpdate() > 0
        );
    }


    /**
     * Заполняет объект PreparedStatement Значениями из объекта Author
     *
     * @param stmt     PreparedStatement, который нужно заполнить
     * @param author   объект Author, содержащий данные
     * @param isUpdate true, если операция обновления, false для операции вставки
     * @throws SQLException в случае ошибки доступа к базе данных
     */
    private void fillPreparedStatementForAuthor(PreparedStatement stmt, Author author, boolean isUpdate) throws SQLException {
        stmt.setString(1, author.getName());
        stmt.setObject(2, author.getBirthYear(), Types.INTEGER);
        if (isUpdate) {
            stmt.setInt(3, author.getAuthorId());
        }
    }

    /**
     * Преобразует текущую строку объекта ResultSet  объект Author
     *
     * @param rs объект ResultSet, уже установленный на нужную строку
     * @return объект Author, полученный из данных ResultSet
     * @throws SQLException в случае ошибки доступа к базе данных
     */
    private Author mapResultSetToAuthor(ResultSet rs) throws SQLException {
        Author author = new Author();
        author.setAuthorId(rs.getInt("author_id"));
        author.setName(rs.getString("name"));
        author.setBirthYear((Integer) rs.getObject("birth_year"));
        return author;
    }
}
